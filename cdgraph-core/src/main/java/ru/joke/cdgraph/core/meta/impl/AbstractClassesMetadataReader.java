package ru.joke.cdgraph.core.meta.impl;

import ru.joke.cdgraph.core.meta.ClassMetadata;
import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/*
 * See class file structure in https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html.
 */
abstract class AbstractClassesMetadataReader<T> implements ClassesMetadataReader<T> {

    protected static final String CLASS_EXTENSION = ".class";
    protected static final String DEFAULT_PACKAGE = "default";

    protected static final int ACC_INTERFACE = 0x0200;
    protected static final int ACC_ABSTRACT = 0x0400;
    protected static final int ACC_ANNOTATION = 0x2000;

    protected ClassMetadata read(@Nonnull InputStream classStream, @Nonnull String classNameRelativePath) {

        final int classNameIdx = classNameRelativePath.lastIndexOf('/');
        final String className = classNameRelativePath.substring(classNameIdx + 1).replace(CLASS_EXTENSION, "");
        final String packageName = classNameRelativePath.substring(0, classNameIdx == -1 ? classNameRelativePath.length() : classNameIdx).replace('/', '.');

        try (final DataInputStream dis = new DataInputStream(classStream)) {

            /* magic word (4) + minor version (2) + major version (2) */
            dis.readNBytes(4 + 2 + 2);

            /* 1 byte to constants pool size */
            final int constantPoolCount = dis.readUnsignedShort();
            skipConstantPool(dis, constantPoolCount);

            /* 1 byte to access_flags section */
            final int accessFlags = dis.readUnsignedShort();

            return new ClassMetadata(className, detectClassType(accessFlags), packageName.isEmpty() ? DEFAULT_PACKAGE : packageName);
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
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
             * 15 = CONSTANT_MethodDescriptor_info
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
