package ru.joke.cdgraph.maven.params.eval;

import javax.annotation.Nonnull;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class GeneratedClassesFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private final Map<String, JavaClassAsBytes> compiledClasses;
    private final ClassLoader generatedClassesLoader;

    GeneratedClassesFileManager(@Nonnull JavaFileManager fileManager) {
        super(fileManager);
        this.compiledClasses = new ConcurrentHashMap<>();
        this.generatedClassesLoader = new GeneratedClassesClassLoader(getClass().getClassLoader(), this);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling) {

        final var classAsBytes = new JavaClassAsBytes(className, kind);
        this.compiledClasses.put(className, classAsBytes);

        return classAsBytes;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return this.generatedClassesLoader;
    }

    public JavaClassAsBytes getClassByName(@Nonnull String name) {
        return compiledClasses.get(name);
    }
}
