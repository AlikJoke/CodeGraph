package ru.joke.cdgraph.starter.console;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersFactory;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.CodeGraphFactory;
import ru.joke.cdgraph.core.client.CodeGraphRequest;
import ru.joke.cdgraph.core.characteristics.impl.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicParametersFactory;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraphFactory;
import ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraphFactory;
import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.starter.shared.ClassesMetadataReaderType;
import ru.joke.cdgraph.starter.shared.CodeGraphDataSourceType;
import ru.joke.cdgraph.starter.shared.CodeGraphType;

import javax.annotation.Nonnull;
import java.net.PasswordAuthentication;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

final class ConsoleCodeGraphRequest implements CodeGraphRequest {

    private final Map<String, String> parameters;
    private final CodeGraphCharacteristicFactoryRegistry factoryRegistry;
    private final CodeGraphCharacteristicParametersFactory parametersFactory;
    private final CodeGraphCache codeGraphCache;

    ConsoleCodeGraphRequest(@Nonnull Map<String, String> parameters) {
        this.parameters = parameters;

        this.factoryRegistry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        this.factoryRegistry.register(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        this.parametersFactory = new SimpleCodeGraphCharacteristicParametersFactory();
        this.codeGraphCache = CodeGraphCache.SINGLETON;
    }

    @Nonnull
    @Override
    public CodeGraph codeGraph() {
        final var codeGraphFactory = createCodeGraphFactory();

        final var dataSourceType = CodeGraphDataSourceType.from(this.parameters.get(DATASOURCE_TYPE));
        final var dataSourcePathString = this.parameters.get(DATA_PATH);

        final var useCache = Boolean.parseBoolean(this.parameters.get(CACHE_GRAPH));
        final var codeGraphKey = createGraphKey(dataSourcePathString, dataSourceType, codeGraphFactory);
        return this.codeGraphCache.compute(codeGraphKey, useCache, () -> {

            final var dataSourceFactory = createDataSourceFactory(dataSourceType);

            final var classesMetadataReader = createClassesMetadataReader(dataSourceType);

            final var dataSourcePath = Path.of(dataSourcePathString);
            final var dataSource = dataSourceFactory.create(dataSourcePath, classesMetadataReader);

            return codeGraphFactory.create(dataSource);
        });
    }

    @Nonnull
    @Override
    public List<CodeGraphCharacteristic<?>> requiredCharacteristics() {
        final var characteristics = this.parameters.get(CHARACTERISTICS);
        return Arrays.stream(characteristics.split(";"))
                        .map(this::createCharacteristic)
                        .collect(Collectors.toList());
    }

    private CodeGraphCharacteristic<?> createCharacteristic(final String characteristicRequest) {
        final var characteristicParts = characteristicRequest.split(":");
        final var characteristicId = characteristicParts[0];

        final Map<String, String> params = convertToParametersMap(characteristicRequest, characteristicParts, characteristicId);

        final var factory = this.factoryRegistry.find(characteristicId);
        final var characteristicParameters = this.parametersFactory.createFor(factory, params);

        return factory.createCharacteristic(characteristicParameters);
    }

    private static Map<String, String> convertToParametersMap(
            final String characteristicRequest,
            final String[] characteristicParts,
            final String characteristicId) {

        final var characteristicParams =
                characteristicParts.length == 1
                        ? new String[0]
                        : characteristicRequest.substring(characteristicId.length() + 1).split(",");

        final Map<String, String> params = new HashMap<>();
        for (var characteristicParam : characteristicParams) {
            final var characteristicParamParts = characteristicParam.split("=");
            params.put(characteristicParamParts[0], characteristicParamParts[1]);
        }

        return params;
    }

    private <T> ClassesMetadataReader<T> createClassesMetadataReader(final CodeGraphDataSourceType dataSourceType) {
        return dataSourceType == CodeGraphDataSourceType.DIRECTORY
                ? ClassesMetadataReaderType.DIRECTORY.createReader()
                : ClassesMetadataReaderType.JAR.createReader();
    }

    private CodeGraphFactory createCodeGraphFactory() {

        final var codeGraphType = CodeGraphType.from(this.parameters.get(GRAPH_TYPE));
        return switch (codeGraphType) {
            case MAVEN_MODULES -> {
                final var repositoryUrl = this.parameters.get(MAVEN_REPO_BASE_URL);
                yield repositoryUrl == null
                        ? new MavenModuleCodeGraphFactory()
                        : new MavenModuleCodeGraphFactory(repositoryUrl, composeCredentials());
            }
            case JAVA_MODULES -> new JavaModuleCodeGraphFactory();
        };
    }

    private <T> CodeGraphDataSourceFactory<T> createDataSourceFactory(final CodeGraphDataSourceType dataSourceType) {
        final var nestedDataSourceType =
                this.parameters.get(NESTED_DATASOURCE_TYPE) == null
                        ? null
                        : CodeGraphDataSourceType.from(this.parameters.get(NESTED_DATASOURCE_TYPE));
        return CodeGraphDataSourceType.createFactory(dataSourceType, nestedDataSourceType);
    }

    private String createGraphKey(
            final String dataPath,
            final CodeGraphDataSourceType dataSourceType,
            final CodeGraphFactory graphFactory) {
        return dataPath + "%" + dataSourceType.name() + "%" + graphFactory.getClass().getSimpleName();
    }

    private PasswordAuthentication composeCredentials() {
        final var repositoryLogin = this.parameters.get(MAVEN_REPO_USER);
        final var repositoryPwd = this.parameters.get(MAVEN_REPO_PWD);

        return repositoryLogin == null || repositoryLogin.isBlank() || repositoryPwd == null
                ? null
                : new PasswordAuthentication(repositoryLogin, repositoryPwd.toCharArray());
    }

    private enum CodeGraphCache {

        SINGLETON;

        private final Map<String, CodeGraph> cache = new ConcurrentHashMap<>();

        @Nonnull
        CodeGraph compute(@Nonnull String id, boolean useCache, @Nonnull Supplier<CodeGraph> graphSupplier) {
            return useCache ? this.cache.computeIfAbsent(id, graphKey -> graphSupplier.get()) : graphSupplier.get();
        }
    }
}
