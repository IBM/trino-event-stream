/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.plugin.eventstream;

import io.prestosql.spi.eventlistener.EventListenerFactory;
import org.testng.annotations.Test;

import static com.google.common.collect.Iterables.getOnlyElement;
import static io.airlift.testing.Assertions.assertInstanceOf;

public class TestEventStreamPlugin
{
    @Test
    public void testCreate()
    {
        EventStreamPlugin plugin = new EventStreamPlugin();

        EventListenerFactory factory = getOnlyElement(plugin.getEventListenerFactories());

        assertInstanceOf(factory, EventStreamEventListenerFactory.class);
    }
}
