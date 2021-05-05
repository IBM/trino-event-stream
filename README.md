# Trino event stream

[Development guide](DEVELOPMENT.md)

A Trino plugin to stream trino events into a Kafka topic.

It implements the [`io.trino.spi.eventlistern.EventListener`](https://github.com/trinodb/trino/blob/f6422d04663ab011d3ddb831ed16dec02659c47e/core/trino-spi/src/main/java/io/trino/spi/eventlistener/EventListener.java) interface.

## Install

Run `mvn install` to build this plugin, then put the plugin file
`trino-event-stream-352.zip` to the plugin folder of trino server.

## Configuration

Create new properties file `event-listener.properties` inside the `/etc/trino` directory:

```
event-listener.name=event-stream
bootstrap.servers=broker:9092
key.serializer=org.apache.kafka.common.serialization.StringSerializer
value.serializer=org.apache.kafka.common.serialization.StringSerializer
```

Avro formatter is added to serialize messages generated from QueryCreatedEvent, QueryCompletedEvent. Avro formatted messages would be read as String using the StringSerializer
Then it will emit events to the Kafka topic `trino.event`.

