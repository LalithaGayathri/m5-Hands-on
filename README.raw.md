# TODO

## Description

This project is a starter for the tax-calculator + order-API exercises. The repository includes Java sources under `src`, tests under `test`, and Makefile targets for dependency setup, compilation, testing, coverage, and mutation testing. The `OrderApi` class uses fake Spring annotations so it compiles without Spring on the classpath, and the project also includes work related to JaCoCo coverage and PIT mutation testing.

## Build

The project uses the Makefile for build and test tasks:

- `make deps` downloads the JUnit console jar to `libs/`
- `make build` compiles the Java sources and tests with `javac --release 17`
- `make test` runs the test suite
- `make coverage` runs tests with JaCoCo and writes the report to `coverage/index.html`
- `make mutation` runs PIT and writes the report to `build/reports/pitest/index.html`
- `make clean` removes generated build artifacts

Example:

```bash
make deps && make test
make coverage
make mutation