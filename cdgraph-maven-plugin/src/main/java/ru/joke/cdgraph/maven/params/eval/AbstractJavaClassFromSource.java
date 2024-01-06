package ru.joke.cdgraph.maven.params.eval;

import javax.annotation.Nonnull;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

abstract class AbstractJavaClassFromSource extends SimpleJavaFileObject {

    private static final String STRING_SOURCE = "string:///";

    AbstractJavaClassFromSource(@Nonnull String className, @Nonnull Kind kind) {
        super(URI.create(prepareSource(className, kind)), kind);
    }

    @Nonnull
    private static String prepareSource(@Nonnull final String className, @Nonnull final Kind kind) {
        return STRING_SOURCE + className.replace('.', '/') + kind.extension;
    }
}
