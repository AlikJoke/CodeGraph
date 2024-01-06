package ru.joke.cdgraph.maven.params.eval;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

final class JavaClassAsBytes extends AbstractJavaClassFromSource {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    JavaClassAsBytes(@Nonnull String name, @Nonnull Kind kind) {
        super(name, kind);
    }

    @Nonnull
    public byte[] getBytes() {
        return bos.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
        return bos;
    }
}
