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

import io.airlift.log.Logger;
import io.trino.spi.eventlistener.EventListener;
import io.trino.spi.eventlistener.QueryCompletedEvent;
import io.trino.spi.eventlistener.QueryCreatedEvent;
import io.trino.spi.eventlistener.SplitCompletedEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * An EventListener wraps Kafka producer to send query events to Kafka
 */
public class EventStreamEventListener
        implements EventListener
{
    private static final Logger log = Logger.get(EventStreamEventListener.class);
    private final KafkaProducer kafkaProducer;
    // TODO make this topic name configurable
    private static final String TOPIC_PRESTO_EVENT = "trino.event";

    public EventStreamEventListener(KafkaProducer<String, Object> kafkaProducer)
    {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void queryCreated(QueryCreatedEvent queryCreatedEvent)
    {
        QueryCreatedEventV1 created = QueryCreatedEventV1.newBuilder()
                .setQueryType(queryCreatedEvent.toString().split("\\[")[0])
                .setQuery(queryCreatedEvent.getMetadata().getQuery())
                .setQueryID(queryCreatedEvent.getMetadata().getQueryId())
                .setPrinciple(queryCreatedEvent.getContext().getPrincipal().toString())
                .setUserAgent(queryCreatedEvent.getContext().getUserAgent().toString())
                .setRemoteClientAddress(queryCreatedEvent.getContext().getRemoteClientAddress().toString())
                .setClientInfo(queryCreatedEvent.getContext().getClientInfo().toString())
                .build();

        try {
            kafkaProducer.send(
                    new ProducerRecord<>(TOPIC_PRESTO_EVENT,
                            queryCreatedEvent.toString(),
                            created.toString()));
        }
        catch (Exception e) {
            log.error(e);
        }
        log.debug("Sent queryCreated event. query id %s", queryCreatedEvent.getMetadata().getQueryId());
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent)
    {
        QueryCompletedEventV1 completed = QueryCompletedEventV1.newBuilder()
                .setQueryType(queryCompletedEvent.toString().split("\\[")[0])
                .setQuery(queryCompletedEvent.getMetadata().getQuery())
                .setQueryID(queryCompletedEvent.getMetadata().getQueryId())
                .setQueryStartTime(queryCompletedEvent.getCreateTime().toString())
                .setQueryEndTime(queryCompletedEvent.getEndTime().toString())
                .setOutputRows(queryCompletedEvent.getStatistics().getOutputRows())
                .build();
        try {
            kafkaProducer.send(
                    new ProducerRecord<>(TOPIC_PRESTO_EVENT,
                            queryCompletedEvent.toString(),
                            completed.toString()));
        }
        catch (Exception e) {
            log.error(e);
        }
        log.debug("Sent queryCompleted event. query id %s", queryCompletedEvent.getMetadata().getQueryId());
    }

    @Override
    public void splitCompleted(SplitCompletedEvent splitCompletedEvent)
    {
        SplitCompletedEventV1 split = SplitCompletedEventV1.newBuilder()
                .setQueryType(splitCompletedEvent.toString().split("\\[")[0])
                .setQueryID(splitCompletedEvent.getQueryId().toString())
                .setQueryStartTime(splitCompletedEvent.getCreateTime().toString())
                .setQueryEndTime(splitCompletedEvent.getEndTime().toString())
                .build();
        try {
            kafkaProducer.send(
                    new ProducerRecord<>(TOPIC_PRESTO_EVENT,
                            splitCompletedEvent.toString(),
                            split.toString()));
        }
        catch (Exception e) {
            log.error(e);
        }
        log.debug("Sent splitCompleted event. query id %s", splitCompletedEvent.getQueryId());
    }
}
