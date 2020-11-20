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

import io.prestosql.spi.eventlistener.EventListener;
import io.prestosql.spi.eventlistener.QueryCompletedEvent;
import io.prestosql.spi.eventlistener.QueryCreatedEvent;
import io.prestosql.spi.eventlistener.SplitCompletedEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * An EventListener wraps Kafka producer to send query events to Kaka
 */
public class EventStreamEventListener
        implements EventListener
{
    private final KafkaProducer kafkaProducer;
    // TODO make this topic configurable
    private static final String TOPIC_PRESTO_EVENT = "presto.event";

    public EventStreamEventListener(KafkaProducer<byte[], byte[]> kafkaProducer)
    {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void queryCreated(QueryCreatedEvent queryCreatedEvent)
    {
        producer.send(
                new ProducerRecord<>(TOPIC_PRESTO_EVENT, queryCreatedEvent.getMetadata().getQueryId(), queryCreatedEvent)
        );
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent)
    {
        producer.send(
                new ProducerRecord<>(TOPIC_PRESTO_EVENT, queryCompletedEvent.getMetadata().getQueryId(), queryCompletedEvent)
        );
    }

    @Override
    public void splitCompleted(SplitCompletedEvent splitCompletedEvent)
    {
        producer.send(
                new ProducerRecord<>(TOPIC_PRESTO_EVENT, splitCompletedEvent.getQueryId(), splitCompletedEvent)
        );
    }
}