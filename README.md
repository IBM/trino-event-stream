# Trino event stream

[Development guide](DEVELOPMENT.md)

A Trino plugin to stream trino events into a Kafka topic.

It implements the [`io.trino.spi.eventlistern.EventListener`](https://github.com/trinodb/trino/blob/f6422d04663ab011d3ddb831ed16dec02659c47e/core/trino-spi/src/main/java/io/trino/spi/eventlistener/EventListener.java) interface.