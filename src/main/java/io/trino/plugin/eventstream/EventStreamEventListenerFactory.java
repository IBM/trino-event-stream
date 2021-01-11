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
package io.trino.plugin.eventstream;

import com.google.common.collect.ImmutableMap;
import io.trino.spi.eventlistener.EventListener;
import io.trino.spi.eventlistener.EventListenerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Iterator;
import java.util.Map;

public class EventStreamEventListenerFactory
        implements EventListenerFactory
{
    private static final String REGEX_CONFIG_PREFIX = "^event-stream.";

    @Override
    public String getName()
    {
        return "event-stream";
    }

    @Override
    public EventListener create(Map<String, String> config)
    {
        KafkaProducer<String, Object> kafkaProducer =
                createKafkaProducer(toKafkaConfig(config));

        return new EventStreamEventListener(kafkaProducer);
    }

    /**
     * Transform event listener configuration into a Kafka configuration.
     * @param config event listener configuration object
     * @return Map<String, Object>
     */
    private static Map<String, Object> toKafkaConfig(Map<String, String> config)
    {
        ImmutableMap.Builder builder = ImmutableMap.<String, Object>builder();

        Iterator<String> it = config.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            String kafkaConfigKey = key.replaceFirst(REGEX_CONFIG_PREFIX,
                    "");
            builder.put(kafkaConfigKey, config.get(key));
        }

        // TODO design ways to config/code serializer

        return builder.build();
    }

    private KafkaProducer<String, Object> createKafkaProducer(Map<String, Object> properties)
    {
        return new KafkaProducer<>(properties);
    }
}
