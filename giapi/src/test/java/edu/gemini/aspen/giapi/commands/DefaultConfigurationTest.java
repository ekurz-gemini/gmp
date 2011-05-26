package edu.gemini.aspen.giapi.commands;

import com.gargoylesoftware.base.testing.EqualsTester;
import com.google.common.collect.ImmutableSortedMap;
import org.junit.Test;

import java.util.SortedMap;

import static edu.gemini.aspen.giapi.commands.ConfigPath.EMPTY_PATH;
import static edu.gemini.aspen.giapi.commands.ConfigPath.configPath;
import static edu.gemini.aspen.giapi.commands.DefaultConfiguration.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DefaultConfiguration} class
 */
public class DefaultConfigurationTest {
    @Test
    public void testBasicConfigurationWithSimplePath() {
        Configuration configuration = configuration(configPath("X"), "value1");
        assertEquals(1, configuration.getKeys().size());
        assertEquals("{config={X=value1}}", configuration.toString());
        assertEquals("value1", configuration.getValue(configPath("X")));
        assertNull(configuration.getValue(configPath("Y")));
        assertTrue(configuration.getKeys().contains(configPath("X")));
        assertFalse(configuration.getKeys().contains(configPath("Y")));
        //todo: this test needs an update
        assertEquals(configuration, configuration.getSubConfiguration(configPath("X")));
        assertEquals(configuration, configuration.getSubConfiguration(EMPTY_PATH));
    }

    @Test
    public void testBasicConfigurationWithHierarchicalPath() {
        Configuration configuration = configurationBuilder()
                .withPath(configPath("gpi:cc"), "value1")
                .withPath(configPath("gpi:aoc"), "value2")
                .build();
        assertEquals(2, configuration.getKeys().size());
        assertEquals("value1", configuration.getValue(configPath("gpi:cc")));
        assertEquals("value2", configuration.getValue(configPath("gpi:aoc")));
        assertNull(configuration.getValue(configPath("Y")));
        assertTrue(configuration.getKeys().contains(configPath("gpi:cc")));
        assertTrue(configuration.getKeys().contains(configPath("gpi:aoc")));
        assertFalse(configuration.getKeys().contains(configPath("Y")));

        assertEquals(emptyConfiguration(), configuration.getSubConfiguration(configPath("X")));
        assertEquals(configuration, configuration.getSubConfiguration(configPath("gpi")));

        Configuration ccConfiguration = configurationBuilder()
                .withPath(configPath("gpi:cc"), "value1")
                .build();
        assertEquals(ccConfiguration, configuration.getSubConfiguration(configPath("gpi:cc")));

        Configuration aocConfiguration = configurationBuilder()
                .withPath(configPath("gpi:aoc"), "value2")
                .build();
        assertEquals(aocConfiguration, configuration.getSubConfiguration(configPath("gpi:aoc")));
    }

    @Test
    public void testEquality() {
        Configuration a = configuration(configPath("X", "val1"), "1");
        Configuration b = configuration(configPath("X", "val1"), "1");
        Configuration c = configuration(configPath("X", "val2"), "1");

        new EqualsTester(a, b, c, null);
    }

    @Test
    public void testBuilderWithPath() {
        Configuration configuration = configuration(configPath("X"), "value1");
        Configuration copy = copy(configuration).build();
        assertEquals(configuration, copy);

        Configuration modifiedCopy = copy(configuration).withPath(configPath("Y"), "value2").build();
        assertFalse(copy.equals(modifiedCopy));

        SortedMap<ConfigPath, String> baseConfig = ImmutableSortedMap.of(configPath("X"), "value1", configPath("Y"), "value2");
        Configuration referenceConfig = new DefaultConfiguration(baseConfig);

        assertEquals(modifiedCopy, referenceConfig);
    }

    @Test
    public void testBuilderWithConfiguration() {
        SortedMap<ConfigPath, String> baseConfig = ImmutableSortedMap.of(configPath("X"), "value1", configPath("Y"), "value2");
        Configuration referenceConfig = new DefaultConfiguration(baseConfig);

        Configuration config = configurationBuilder()
                .withConfiguration("X", "value1")
                .withConfiguration("Y", "value2")
                .build();

        assertEquals(referenceConfig, config);
    }

    @Test
    public void testEmptyConfiguration() {
        assertTrue(emptyConfiguration().getKeys().isEmpty());
        assertEquals(emptyConfiguration(), emptyConfiguration());
        assertEquals(emptyConfiguration(), emptyConfiguration().getSubConfiguration(null));
        assertTrue(emptyConfiguration().isEmpty());
    }

}
