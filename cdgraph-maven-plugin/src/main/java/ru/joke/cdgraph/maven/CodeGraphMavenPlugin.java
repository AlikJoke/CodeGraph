package ru.joke.cdgraph.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersFactory;
import ru.joke.cdgraph.core.characteristics.impl.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicParametersFactory;
import ru.joke.cdgraph.core.client.CodeGraphOutputSink;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.client.CodeGraphRequest;
import ru.joke.cdgraph.core.client.CodeGraphSinkException;
import ru.joke.cdgraph.core.client.impl.*;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.impl.CodeGraphFileSystemDataSourceFactory;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.graph.CodeGraphFactory;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraphFactory;
import ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraphFactory;
import ru.joke.cdgraph.maven.params.MavenCodeGraphCharacteristicParameters;
import ru.joke.cdgraph.maven.params.MavenRepository;

import java.io.*;
import java.net.PasswordAuthentication;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraph.POM;

/**
 * Plugin for calculating the characteristics of a modules graph.<br>
 * Must connect to one of the end projects with packaging type like ear, war or jar.<br>
 * Configuration parameters:
 * <ul>
 *     <li>{@literal graphType}: Type of the graph to build; required parameter</li>
 *     <li>{@literal outputFormat}: Type of the output format; required parameter</li>
 *     <li>{@literal outputFile}: Type of the output format; when the parameter is not specified,
 *     console output is used</li>
 *     <li>{@literal characteristics}: List of the required characteristics; required parameter</li>
 *     <li>{@literal failOnErrors}: Whether the plugin should abort the build on errors; default value is {@code true}</li>
 *     <li>{@literal failOnOutOfBounds}: Whether the plugin should abort the build when the
 *     characteristic's computed values out of bounds of the configured min/max characteristic
 *     value; default value is {@code true}</li>
 *     <li>{@literal mavenRepository}: Custom maven repository information; when the parameter is not
 *     specified, central maven repository is used. Nested parameters:
 *     <ul>
 *         <li>{@literal url}: Url of the maven repository; required parameter</li>
 *         <li>{@literal login}: User to access to the maven repository; optional parameter</li>
 *         <li>{@literal password}: Password to access to the maven repository; optional parameter;
 *         in the configuration, the password can be set via the property (${...})</li>
 *     </ul>
 *     </li>
 * </ul>
 *
 * @author Alik
 * @see CodeGraph
 * @see ru.joke.cdgraph.core.client.CodeGraphClient
 */
@Mojo(name = "code-graph-analysis", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public final class CodeGraphMavenPlugin extends AbstractMojo {

    private static final String JAVA_MODULES = "java-modules";
    private static final String MAVEN_MODULES = "maven-modules";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(required = true)
    private String graphType;

    @Parameter(required = true)
    private String outputFormat;

    @Parameter
    private File outputFile;

    @Parameter(required = true)
    private Map<String, MavenCodeGraphCharacteristicParameters> characteristics;

    @Parameter(defaultValue = "true")
    private boolean failOnErrors;

    @Parameter(defaultValue = "true")
    private boolean failOnOutOfBounds;

    @Parameter
    private MavenRepository mavenRepository;

    @Override
    public void execute() throws MojoFailureException {
        checkModuleType();

        try {
            executeRequest();
        } catch (RuntimeException ex) {
            handleException(ex);
        }
    }

    private void checkModuleType() throws MojoFailureException {
        if (POM.equalsIgnoreCase(this.project.getModel().getPackaging())) {
            throw new MojoFailureException("Plugin must be applied to the project with packaging types: war, ear, jar");
        }
    }

    private void executeRequest() {

        final var outputSpecification = createOutputSpecification();
        final var request = createGraphRequest();

        final var client = new SimpleCodeGraphClient();
        client.execute(request, outputSpecification);
    }

    private void handleException(final RuntimeException ex) throws MojoFailureException {
        final var sink = createSink();
        try (sink) {
            sink.write(convertExceptionToString(ex));
        }

        if (this.failOnErrors) {
            throw new MojoFailureException(ex);
        }
    }

    private String convertExceptionToString(final RuntimeException exception) {
        try (final var writer = new StringWriter(); final var pw = new PrintWriter(writer)) {
            exception.printStackTrace(pw);

            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CodeGraphRequest createGraphRequest() {
        final var codeGraph = createCodeGraph();
        final var characteristics = createCharacteristics();

        return new SimpleCodeGraphRequest(codeGraph, characteristics);
    }

    private CodeGraphOutputSpecification createOutputSpecification() {
        final var outputFormat = CodeGraphOutputSpecification.Format.from(this.outputFormat);
        final var sink = createSink();

        return new SimpleCodeGraphOutputSpecification(outputFormat, sink);
    }

    private CodeGraphOutputSink createSink() {
        try {
            return this.outputFile == null ? new CodeGraphOutputStreamSink() : new CodeGraphOutputFileSink(this.outputFile);
        } catch (FileNotFoundException ex) {
            throw new CodeGraphSinkException(ex);
        }
    }

    private CodeGraph createCodeGraph() {
        final var codeGraphFactory = createCodeGraphFactory();
        final var dataSource = createDataSource();

        return codeGraphFactory.create(dataSource);
    }

    private CodeGraphDataSource createDataSource() {
        final var directoryDataSourceFactory = new CodeGraphFileSystemDataSourceFactory();
        final var dataPath = findDataPath();
        return directoryDataSourceFactory.create(dataPath);
    }

    private Path findDataPath() {
        final var packaging = this.project.getModel().getPackaging();

        MavenProject lastModel = this.project;
        while (lastModel.getParent() != null) {
            lastModel = lastModel.getParent();
        }

        final var nestedProjects = lastModel.getCollectedProjects();
        Path result = lastModel.getBasedir().toPath();
        for (final var nestedProject : nestedProjects) {
            result = findCommonPath(result, nestedProject.getBasedir().toPath());
        }

        return result;
    }

    private Path findCommonPath(final Path path1, final Path path2) {
        Path relativePath = path1.relativize(path2).normalize();

        while (relativePath != null && !relativePath.endsWith("..")) {
            relativePath = relativePath.getParent();
        }

        return relativePath == null ? path1 : path1.resolve(relativePath).normalize();
    }

    private List<CodeGraphCharacteristic<?>> createCharacteristics() {
        final var factoryRegistry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        factoryRegistry.register(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        // TODO out of configured bounds checks
        final var parametersFactory = new SimpleCodeGraphCharacteristicParametersFactory();
        return this.characteristics.entrySet()
                .stream()
                .map(e -> createCharacteristic(e.getKey(), e.getValue(), factoryRegistry, parametersFactory))
                .collect(Collectors.toList());
    }

    private CodeGraphCharacteristic<?> createCharacteristic(
            final String characteristicId,
            final MavenCodeGraphCharacteristicParameters pluginParameters,
            final CodeGraphCharacteristicFactoryRegistry factoryRegistry,
            final CodeGraphCharacteristicParametersFactory parametersFactory) {

        final var factory = factoryRegistry.find(characteristicId);
        final var parameters = parametersFactory.createFor(factory, pluginParameters.getParameters());

        return factory.createCharacteristic(parameters);
    }

    private CodeGraphFactory createCodeGraphFactory() {
        return switch (this.graphType) {
            case JAVA_MODULES -> new JavaModuleCodeGraphFactory();
            case MAVEN_MODULES ->
                    mavenRepository == null
                            ? new MavenModuleCodeGraphFactory()
                            : new MavenModuleCodeGraphFactory(mavenRepository.getUrl(), composeCredentials());
            default -> throw new CodeGraphConfigurationException("Unsupported graph type: " + this.graphType);
        };
    }

    private PasswordAuthentication composeCredentials() {
        final var repositoryLogin = this.mavenRepository.getLogin();
        final var repositoryPwd = this.mavenRepository.getPassword();

        return repositoryLogin == null || repositoryLogin.isBlank() || repositoryPwd == null
                ? null
                : new PasswordAuthentication(repositoryLogin, repositoryPwd.toCharArray());
    }

    public String getGraphType() {
        return graphType;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public Map<String, MavenCodeGraphCharacteristicParameters> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Map<String, MavenCodeGraphCharacteristicParameters> characteristics) {
        this.characteristics = characteristics;
    }

    public boolean isFailOnErrors() {
        return failOnErrors;
    }

    public void setFailOnErrors(boolean failOnErrors) {
        this.failOnErrors = failOnErrors;
    }

    public boolean isFailOnOutOfBounds() {
        return failOnOutOfBounds;
    }

    public void setFailOnOutOfBounds(boolean failOnOutOfBounds) {
        this.failOnOutOfBounds = failOnOutOfBounds;
    }

    public MavenRepository getMavenRepository() {
        return mavenRepository;
    }

    public void setMavenRepository(MavenRepository mavenRepository) {
        this.mavenRepository = mavenRepository;
    }
}
