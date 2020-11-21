package io.prestosql.plugin.eventstream;

import io.prestosql.spi.eventlistener.EventListenerFactory;
import org.testng.annotations.Test;

import static com.google.common.collect.Iterables.getOnlyElement;
import static io.airlift.testing.Assertions.assertInstanceOf;

public class TestEventStreamPlugin
{
    @Test
    public void testCreate() {
        EventStreamPlugin plugin = new EventStreamPlugin();

        EventListenerFactory factory = getOnlyElement(plugin.getEventListenerFactories());

        assertInstanceOf(factory, EventStreamEventListenerFactory.class);
    }
}
