# Development

Developers should follow this [development guide](https://github.com/IBM/presto-db2/blob/main/DEVELOPMENT.md).

## Release
First update the `main` branch of this repo via PR process. Then, go to https://github.com/IBM/trino-event-stream/releases to draft your release. Configure the release to create a new branch named after the Trino version (e.g. 372). Before publishing the release, build the plugin locally with `mvn clean install`, and upload the resulting archive `target/trino-db2-[version].zip` to the release binaries. Then, you may click "publish release".