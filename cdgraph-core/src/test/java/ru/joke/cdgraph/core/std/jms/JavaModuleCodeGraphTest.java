package ru.joke.cdgraph.core.std.jms;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.*;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.module.ModuleDescriptor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class JavaModuleCodeGraphTest {

    @Test
    public void testSingleModuleWithoutDependencies() {
        final CodeGraphDataSource dataSource = createCodeGraph("modules/module-info1.class");

        final CodeGraph graph = new JavaModuleCodeGraph(dataSource);

        assertNotNull(graph.rootNode(), "Root node must be not null");
        assertEquals("ru.joke.cdgraph.test_modules1", graph.rootNode().id(), "Root node id must be equal");
        assertEquals(1, graph.rootNode().dependencies().size(), "Module should dependent only from base module");

        final GraphNodeRelation dependencyRelation = graph.rootNode().dependencies().iterator().next();
        assertEquals(graph.rootNode(), dependencyRelation.source(), "Source node must be equal to root");
        assertEquals(2, graph.rootNode().tags().size(), "Root node tags count must be equal");
        final Map<String, Object> rootNodeTagsMap =
                graph.rootNode().tags()
                                    .stream()
                                    .collect(Collectors.toMap(GraphTag::name, GraphTag::value));
        assertNotNull(rootNodeTagsMap.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag must present");

        final Boolean openTagValue = (Boolean) rootNodeTagsMap.get(ModuleDescriptor.Modifier.OPEN.name().toLowerCase());
        assertTrue(openTagValue, "Root module must be open");

        assertEquals(JavaModuleCodeGraph.REQUIRES_TYPE, dependencyRelation.type(), "Relation type must be equal");
        assertEquals(Object.class.getModule().getName(), dependencyRelation.target().id(), "Target module must be base module");
        assertTrue(dependencyRelation.target().dependencies().isEmpty(), "Dependencies for target module must be empty");

        final Map<String, Object> relationTags =
                dependencyRelation.tags()
                                    .stream()
                                    .collect(Collectors.toMap(GraphTag::name, GraphTag::value));;
        assertEquals(2, relationTags.size(), "Count of tags of relation must be equal");

        final Boolean mandatedTagValue = (Boolean) relationTags.get(ModuleDescriptor.Requires.Modifier.MANDATED.name().toLowerCase());
        assertTrue(mandatedTagValue != null && mandatedTagValue, "Target module relation must be mandated");
        assertNotNull(relationTags.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag for relation must present");
    }

    @Test
    public void testSingleModuleWithDependencies() {
        // TODO
    }

    @Test
    public void testTwoModules() {
        // TODO
    }

    @Test
    public void testThreeModules() {
        // TODO
    }

    @Test
    public void testNoModules() {
        final CodeGraphDataSource dataSource = createCodeGraph();
        assertThrows(CodeGraphConfigurationException.class, () -> new JavaModuleCodeGraph(dataSource));
    }

    private CodeGraphDataSource createCodeGraph(final String... modulePath) {
        return new CodeGraphDataSource() {
            @Override
            @Nonnull
            public Iterator<File> iterator() {
                final List<File> result = new ArrayList<>(modulePath.length);
                for (final String path : modulePath) {
                    final URL moduleDescriptorUrl = getClass().getClassLoader().getResource(path);
                    final File moduleDescriptorFile = new File(moduleDescriptorUrl.getFile());
                    result.add(moduleDescriptorFile);
                }

                return result.iterator();
            }
        };
    }
}
