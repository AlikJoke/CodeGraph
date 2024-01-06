package ru.joke.cdgraph.maven.params.eval;

import javax.annotation.Nonnull;

final class JavaBoundCheckerClassSource extends AbstractJavaClassFromSource {

    private static final String SOURCE_CODE_TEMPLATE = """
            package ru.joke.cdgraph.maven.params.eval;
            
            import java.util.*;
            import ru.joke.cdgraph.core.characteristics.impl.paths.*;
            import ru.joke.cdgraph.core.characteristics.impl.clusters.*;
            import ru.joke.cdgraph.core.characteristics.impl.factors.*;
            import ru.joke.cdgraph.core.characteristics.impl.bridges.*;
            import ru.joke.cdgraph.core.characteristics.impl.counters.*;
            import ru.joke.cdgraph.core.characteristics.impl.locations.*;
            
            final class %s implements CharacteristicResultBoundChecker<%s> {
                @Override
                public boolean check(%s result) {
                    return %s;
                }
            }
            """;

    private final String sourceCode;

    JavaBoundCheckerClassSource(
            @Nonnull String fullClassName,
            @Nonnull String className,
            @Nonnull String boundCheckingCodeSnippet,
            @Nonnull String parameterTypeInfo) {
        super(fullClassName, Kind.SOURCE);
        this.sourceCode = format(className, boundCheckingCodeSnippet, parameterTypeInfo);
    }

    @Override
    @Nonnull
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.sourceCode;
    }

    private static String format(
            final String className,
            final String boundCheckingCodeSnippet,
            final String parameterTypeInfo) {
        return SOURCE_CODE_TEMPLATE.formatted(className, parameterTypeInfo, parameterTypeInfo, boundCheckingCodeSnippet);
    }
}
