# I Dentist

A zero-dependency, HttpURLConnection-based HTTP client in Kotlin for basic use cases.

## Install

### Gradle via JitPack

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
dependencies {
    implementation 'com.github.kgilmer:I-Dentist:f53c21117d636f7a7963c2354030edd2d06e1b64'
}
```

## Features

* No dependencies, utilizes Java's URL and HttpURLConnection classes.
* Integration tests via Jetty
* Did I mention there are no dependencies?
* One file, MIT Licensed.
* API implemented entirely as extension functions.  No new top-level classes or interfaces to deal with.
* Less than 50 LoC.
* Connection is managed by the function, nothing to clean up.
* API enables painless deserialization into native forms:
```kotlin
// Given an object form of some HTTP response
data class MyDataClass(val foo: String)

// And a way of converting the bytes from the server into that form
fun deserialize(input: InputStream): MyDataClass =
        MyDataClass(input.readBytes().toString(Charset.defaultCharset()))

// Using I-Dentist, give me the deserialized object
val data = URL("data.org").httpGet { _, _, body -> deserialize(body!!) }

println(data.foo)
```

## Examples

### HTTP Get

```kotlin
val html = URL("http://somewebsite.com").httpGet(mapOf("some-header" to "myval")) { _, _, body ->
    body?.readBytes().toString(Charset.defaultCharset())
}
```

### HTTP Post

```kotlin
URL("http://somewebsite.com").httpPost("{}".byteInputStream()) { statusCode, headers, body ->
    when (statusCode) {
      200 -> handleSuccess(body!!)
      else -> throw IllegalArgumentException("Something bad happened...")
    }
}
```

## Status

This HTTP client library is very small.  It's mainly just syntax over existing APIs so there is little to validate.  That said, it should be considered experimental.
