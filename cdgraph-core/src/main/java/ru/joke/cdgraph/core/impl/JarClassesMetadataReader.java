package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.ClassesMetadataReader;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph.MODULE_INFO_CLASS;

/*
 * See class file structure in https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html.
 */
public final class JarClassesMetadataReader implements ClassesMetadataReader {

    private static final String CLASS_EXTENSION = ".class";
    private static final String DEFAULT_PACKAGE = "default";

    private static final int ACC_INTERFACE = 0x0200;
    private static final int ACC_ABSTRACT = 0x0400;
    private static final int ACC_ANNOTATION = 0x2000;

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

        final String entryName = classEntry.getName();
        final int classNameIdx = entryName.lastIndexOf('/');
        final String className = entryName.substring(classNameIdx + 1).replace(CLASS_EXTENSION, "");
        final String packageName = entryName.substring(0, classNameIdx).replace('/', '.');

        try (final InputStream is = jar.getInputStream(classEntry);
                final DataInputStream dis = new DataInputStream(is)) {

            /* magic word (4) + minor version (2) + major version (2) */
            dis.readNBytes(4 + 2 + 2);

            /* 1 byte to constants pool size */
            final int constantPoolCount = dis.readUnsignedShort();
            skipConstantPool(dis, constantPoolCount);

            /* 1 byte to access_flags section */
            final int accessFlags = dis.readUnsignedShort();

            return new ClassMetadata(className, detectClassType(accessFlags), packageName.isEmpty() ? DEFAULT_PACKAGE : packageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void skipConstantPool(DataInputStream dataInputStream, int constantPoolCount) throws IOException {
        for (int i = 1; i < constantPoolCount; i++) {
            /* 1 byte to constant type tag */
            final int tag = dataInputStream.readUnsignedByte();
            switch (tag) {
                /*
                 * Constant types with 2-byte data
                 * ===============================
                 * 7 = CONSTANT_Class
                 * 8 = CONSTANT_String
                 * 16 = CONSTANT_MethodType_info
                 */
                case 7, 8, 16 -> dataInputStream.skipBytes(2);
                /*
                 * Constant types with 4-byte data
                 * ===============================
                 * 3 = CONSTANT_Integer
                 * 4 = CONSTANT_Float
                 * 9 = CONSTANT_Fieldref
                 * 10 = CONSTANT_Methodref
                 * 11 = CONSTANT_InterfaceMethodref
                 * 12 = CONSTANT_NameAndType
                 * 18 = CONSTANT_InvokeDynamic
                 */
                case 3, 4, 9, 10, 11, 12, 18 -> dataInputStream.skipBytes(4);
                /*
                 * Constant types with 8-byte data
                 * ===============================
                 * 5 = CONSTANT_Long
                 * 6 = CONSTANT_Double
                 */
                case 5, 6 -> {
                    dataInputStream.skipBytes(8);
                    i++;
                }
                /*
                 * Constant UTF8 type (floating length)
                 * ====================================
                 * 5 = CONSTANT_Utf8
                 */
                case 1 -> dataInputStream.skipBytes(dataInputStream.readUnsignedShort());
                /*
                 * Constant with 3-byte length
                 * ===============================
                 * 15 = CONSTANT_MethodHandle_info
                 */
                case 15 -> dataInputStream.skipBytes(3);
                default -> throw new UnsupportedOperationException("Unsupported constant type: " + tag);
            }
        }
    }

    private ClassMetadata.ClassType detectClassType(final int access) {

        if ((access & ACC_ANNOTATION) != 0) {
            return ClassMetadata.ClassType.ANNOTATION;
        } else if ((access & ACC_INTERFACE) != 0) {
            return ClassMetadata.ClassType.INTERFACE;
        } else if ((access & ACC_ABSTRACT) != 0) {
            return ClassMetadata.ClassType.ABSTRACT;
        } else {
            return ClassMetadata.ClassType.CONCRETE;
        }
    }
}
