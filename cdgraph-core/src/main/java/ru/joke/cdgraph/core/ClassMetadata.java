package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * Metadata of the class from the module (node of the graph).
 *
 * @param className name of the class, can not be {@code null}.
 * @param classType type of the class, can not be {@code null}.
 * @param packageName name of the Java package that contains class, can not be {@code null}.
 *
 * @author Alik
 * @see ClassType
 */
public record ClassMetadata(@Nonnull String className, @Nonnull ClassType classType, @Nonnull String packageName) {

    /**
     * Type of the class from the code module.
     * @author Alik
     */
    public enum ClassType {

        /**
         * Interface class
         */
        INTERFACE,

        /**
         * Abstract class
         */
        ABSTRACT,

        /**
         * Annotation class
         */
        ANNOTATION,

        /**
         * Concrete class (record, enum, final or simple java class)
         */
        CONCRETE;

        /**
         * Returns whether the class is concrete ({@linkplain ClassType#CONCRETE}.
         * @return whether the class is concrete.
         */
        public boolean isConcrete() {
            return this == CONCRETE;
        }
    }
}
