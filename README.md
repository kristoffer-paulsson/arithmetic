# Concurrency Utilities

<img src="https://angelos-project.com/images/angelos.png" alt="Angelos Project™" style="width:120px; height:auto; margin-right:1em; margin-bottom:1em;" align="left">

This library provides a set of concurrency utilities
designed to simplify the use of concurrent execution and
synchronization in Kotlin for the Angelos Project™.

The utility builds on top of Kotlin's coroutines and provides
additional lambda methods and classes to handle
execution of tasks in an idiomatic way.

The library's primary focus is to provide a set of tools that
gives the developer a hassle-free experience without meddling with
the underlying implementation. Methods that can be called from
any context whether suspending or blocking. Which doesn't use
any features that are experimental, and which is available across
all stable Kotlin build targets.

## Features

- **task**: Runs a given lambda as a coroutine exactly once. Can be called from any synchronous code block and executes the lambda asynchronously.
- **schedule**: Schedules a coroutine to run after a specified time delay. Useful for deferred or timed execution.
- **clock**: Executes a lambda periodically at a given interval, based on a specified number of ticks per time duration.
- **loop**: Repeatedly executes a lambda in an infinite loop, yielding to the event loop after each iteration.
- **call**: Monitors another coroutine job and executes a lambda only if the monitored job is cancelled, checking at a specified interval.
- **attend**: Returns a `Steward` instance. The steward sleeps in an infinite loop and executes its lambda on each `wakeUp()` call. Multiple wake-up calls are queued and processed in order.
- **answer**: Returns a `Waitress` instance. The waitress executes its lambda once per `wakeUp()` call, ignoring additional calls until it returns to sleep, making it suitable for debouncing or single-shot events.
- **Dispenser**: A synchronization primitive that encapsulates a shared resource. Dispenses the resource to a single coroutine at a time using a `Mutex`, ensuring safe concurrent access.

## Installation
To use the concurrency utilities in your Kotlin project, add the following dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("org.angproj.conc:angelos-project-conc:<X.Y.Z-SNAPSHOT>")
}
```

## Contributing
We welcome contributions to this project! If you have an idea for a new feature, bug fix, or improvement, please open an issue or submit a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.