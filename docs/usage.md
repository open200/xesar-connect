# Xesar-Connect usage

## Dependencies

You need the following dependencies in  your `build.gradle.kts` file.

### Xesar-Connect

The Xesar-Connect dependency.

```kotlin
dependencies {
    implementation("com.open200:xesar-connect:1.0.0")
}
```

### SLF4J API implementation

A logging framework that provides an implementation of the SLF4J API like `logback`

```kotlin
dependencies {
    implementation("ch.qos.logback:logback-classic")
}
```

### Java security provider

A library to add a Java security provide like `bouncycastle`

```kotlin
dependencies {
    implementation("org.bouncycastle:bcprov-jdk18on")
}
```
