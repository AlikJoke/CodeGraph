package ru.joke.cdgraph.core.std;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.ClassesMetadataReader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.std.jms.JavaModuleCodeGraph.MODULE_INFO_CLASS;

public final class JarClassesMetadataReader implements ClassesMetadataReader {

    private static final String CLASS_EXTENSION = ".class";
    private static final String DEFAULT_PACKAGE = "default";

    private static final ThreadLocal<ClassMetadata> metadata = new ThreadLocal<>();

    @Nonnull
    @Override
    public Set<ClassMetadata> read(@Nonnull JarFile jar) {

        return jar
                .stream()
                .filter(entry -> entry.getName().endsWith(CLASS_EXTENSION) && !entry.getName().endsWith(MODULE_INFO_CLASS))
                .map(entry -> read(jar, entry))
                .collect(Collectors.toSet());
    }

    private ClassMetadata read(@Nonnull JarFile jar, @Nonnull JarEntry classEntry) {

        try (final InputStream is = jar.getInputStream(classEntry)) {

            final ClassReader classReader = new ClassReader(is);

            final String entryName = classEntry.getName();
            final int classNameIdx = entryName.lastIndexOf('/');
            final String className = entryName.substring(classNameIdx + 1).replace(CLASS_EXTENSION, "");
            final String packageName = entryName.substring(0, classNameIdx).replace('/', '.');

            final ClassVisitor classModifierVisitor = new ClassVisitor(Opcodes.ASM9) {
                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    metadata.set(new ClassMetadata(className, detectClassType(access), packageName.isEmpty() ? DEFAULT_PACKAGE : packageName));
                }
            };

            classReader.accept(classModifierVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            return metadata.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            metadata.remove();
        }
    }

    private ClassMetadata.ClassType detectClassType(final int access) {

        if ((access & Opcodes.ACC_ANNOTATION) != 0) {
            return ClassMetadata.ClassType.ANNOTATION;
        } else if ((access & Opcodes.ACC_INTERFACE) != 0) {
            return ClassMetadata.ClassType.INTERFACE;
        } else if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            return ClassMetadata.ClassType.ABSTRACT;
        } else if ((access & Opcodes.ACC_ENUM) != 0) {
            return ClassMetadata.ClassType.ENUM;
        } else {
            return ClassMetadata.ClassType.CONCRETE;
        }
    }
}
