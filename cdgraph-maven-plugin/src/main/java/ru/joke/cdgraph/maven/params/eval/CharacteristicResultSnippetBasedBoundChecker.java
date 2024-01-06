package ru.joke.cdgraph.maven.params.eval;

import org.apache.maven.plugin.logging.Log;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;

import javax.annotation.Nonnull;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CharacteristicResultSnippetBasedBoundChecker {

    private static final String BASE_CHECKER_CLASS_NAME = "CharacteristicResultBoundChecker";
    private static final String BASE_CHECKER_PACKAGE_NAME = "ru.joke.cdgraph.maven.params.eval.";

    private final JavaFileManager generatedClassesFileManager;
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final JavaCompiler compiler;
    private final Log log;

    public CharacteristicResultSnippetBasedBoundChecker(@Nonnull Log log) {
        this.log = log;
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.diagnostics = new DiagnosticCollector<>();
        this.generatedClassesFileManager = new GeneratedClassesFileManager(createStandardFileManager());
    }

    public boolean check(
            @Nonnull CodeGraphCharacteristicResult<?> characteristicResult,
            @Nonnull String boundCheckingCodeSnippet) {

        final var className = BASE_CHECKER_CLASS_NAME + System.nanoTime();
        final var fullClassName = BASE_CHECKER_PACKAGE_NAME + className;
        final var resultTypeInfo = getResultTypeInfo(characteristicResult.get());
        final var sourceFile = new JavaBoundCheckerClassSource(fullClassName, className, boundCheckingCodeSnippet, resultTypeInfo);
        final var task = compiler.getTask(
                null,
                generatedClassesFileManager,
                diagnostics,
                null,
                null,
                List.of(sourceFile)
        );

        if (!task.call()) {
            diagnostics.getDiagnostics()
                        .forEach(d -> log.error(d.toString()));
            throw new CodeGraphCharacteristicConfigurationException("Unable to compile snippets, see previous errors");
        }

        try {
            return makeCheck(characteristicResult.get(), fullClassName);
        } catch (Exception ex) {
            throw new CodeGraphCharacteristicConfigurationException(ex);
        }
    }

    private StandardJavaFileManager createStandardFileManager() {
        final var standardFileManager = compiler.getStandardFileManager(null, null, null);

        try {
            final var classPath = Set.of(getClassJarFile(CodeGraph.class), getClassJarFile(getClass()));
            standardFileManager.setLocation(StandardLocation.CLASS_PATH, classPath);

            return standardFileManager;
        } catch (IOException | URISyntaxException e) {
            throw new CodeGraphConfigurationException(e);
        }
    }

    private File getClassJarFile(final Class<?> clazz) throws URISyntaxException {
        final var codeSource = clazz.getProtectionDomain().getCodeSource();

        final var uri = codeSource.getLocation().toURI();
        return new File(uri.getPath());
    }

    private boolean makeCheck(final Object value, final String className) throws Exception {
        final var classLoader = generatedClassesFileManager.getClassLoader(null);
        final var clazz = classLoader.loadClass(className);
        final var classConstructor = clazz.getDeclaredConstructor();
        classConstructor.setAccessible(true);

        @SuppressWarnings("unchecked")
        final var boundChecker = (CharacteristicResultBoundChecker<Object>) classConstructor.newInstance();
        return boundChecker.check(value);
    }

    private String getResultTypeInfo(final Object value) {
        final String objectType = Object.class.getCanonicalName();
        if (value == null) {
            return objectType;
        }

        final String mainTypeInfo = value.getClass().getCanonicalName();
        return switch (value) {
            case List<?> list -> List.class.getCanonicalName() + getNestedValueTypeInfo(list);
            case Set<?> set -> Set.class.getCanonicalName() + getNestedValueTypeInfo(set);
            case Collection<?> coll -> Collection.class.getCanonicalName() + getNestedValueTypeInfo(coll);
            case Map<?, ?> map -> Map.class.getCanonicalName() + getNestedMapValueTypeInfo(map);
            default -> mainTypeInfo;
        };
    }

    private String getNestedValueTypeInfo(final Collection<?> coll) {
        return "<" + getNestedValueTypeInfo(coll.isEmpty() ? null : coll.iterator().next()) + ">";
    }

    private String getNestedMapValueTypeInfo(final Map<?, ?> map) {
        final String objectType = Object.class.getCanonicalName();
        return "<"
                + (map.isEmpty()
                        ? objectType
                        : getNestedValueTypeInfo(map.keySet().iterator().next()))
                + ", "
                + (map.isEmpty()
                        ? objectType
                        : getNestedValueTypeInfo(map.values().iterator().next()))
                + ">";
    }

    private String getNestedValueTypeInfo(final Object v) {
        return (v == null ? Object.class.getCanonicalName() : v.getClass().getCanonicalName());
    }
}
