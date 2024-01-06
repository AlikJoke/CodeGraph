package ru.joke.cdgraph.maven.params.eval;

final class GeneratedClassesClassLoader extends ClassLoader {

    private final GeneratedClassesFileManager classesFileManager;

    GeneratedClassesClassLoader(ClassLoader parent, GeneratedClassesFileManager manager) {
        super(parent);
        this.classesFileManager = manager;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = this.findLoadedClass(name);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = this.findClass(name);
        } catch (ClassNotFoundException ignored) {
            clazz = super.loadClass(name, resolve);
        }

        if (resolve) {
            this.resolveClass(clazz);
        }

        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        final var compiledClass = this.classesFileManager.getClassByName(name);
        if (compiledClass == null) {
            throw new ClassNotFoundException(name);
        }

        final byte[] bytes = compiledClass.getBytes();
        return defineClass(name, bytes, 0, bytes.length);
    }
}
