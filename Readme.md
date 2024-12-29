# StomGen
## What is StormGen ?

StomGen is a load test tool for message broker.
It now only supports Kafka. It will support more message broker in the future. For example Pulsar, Mqtt, RocketMQ, RabbitMQ are planed to support.

## Motivation

When I try to find some stress testing tools for my kafka consumer application, I can't really find a one really meet my requirements.
Then I decide to write a one.

## Current progress

The project is still in the development.

## How to use ?

Because now the project is still in the development.
If you want to play with current version, clone the project to your local and look at ExampleScenario.scala

## Recent develop plan (sorted by priority)
1. Develop stats collector to collect data of generator and generate a html graph report.
2. Develop build in Scenario. For example Smoke tests, Average-load test, Stress tests, Soak tests, Spike tests, Breakpoint tests.
3. Make application configurable by yaml file.
