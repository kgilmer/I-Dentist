# I Dentist

A zero-dependency, `HttpURLConnection`-based HTTP client in Kotlin for basic use cases.

This library defines a set of extension functions on Java's `java.net.URL` class, each corresponding to an HTTP verb such as *get*.  A URL instance defines the *where* of an HTTP action.  Each extension function defines a function block in which a client can provide a lambda to do work against the response.  The lambda is provided the HTTP status code, the headers in the response, and the optional response body as an input stream.  Once the lambda completes execution, the underlying HTTP connection is closed.  The lambda may also provide a type specification of something desired to be passed back to the client program, such as a deserialized form of the response.

## Features

* No third-party dependencies.
* No globally-addressable classes or functions, but rather extends `java.net.URL`.
* Optional request/response logging via `PrintWriter`.
* Optional custom error handling via `(HttpURLConnection, IOException) -> T`.
* Integration tests via Jetty.
* Did I mention there are no dependencies?
* One file, MIT Licensed.  Copy and paste into your project if you like.
* Less than ~~50~~ 150 LoC.
* Connection is managed by the function, nothing to clean up.
* API enables painless deserialization into native forms.  Example:
```kotlin
// Given an object form of some HTTP response
data class MyDataClass(val foo: String)

// And a way of converting the bytes from the server into that form
fun deserialize(input: InputStream): MyDataClass =
        MyDataClass(input.readBytes().toString(Charset.defaultCharset()))

// Using I-Dentist, give me the deserialized object
val data = URL("bar.org").httpGet { _, _, body -> deserialize(body!!) }

println(data.foo)
```

## Examples

### HTTP GET

```kotlin
val html = URL("http://google.com").httpGet() { _, _, body -> body?.bufferedReader()!!.readText() }
```

### HTTP POST with body and default value.

```kotlin
val someObject = URL("http://somewebsite.com").httpPost("{}".byteInputStream()) { statusCode, _, body ->
    when (statusCode) {
      200 -> handleSuccess(body!!)
      else -> DefaultObjectValue()
    }
}
```

### HTTP PUT with logging
```kotlin
val someObject = URL("http://somewebsite.com").httpPost(body = requestBody, logger = PrintWriter(System.out)) { statusCode, _, body ->
    when (statusCode) {
      200 -> handleSuccess(body!!)
      else -> DefaultObjectValue()
    }
}
```

### HTTP DELETE that throws IOException upon 4xx error
```kotlin
URL("http://somewebsite.com").httpDelete(errorHandler = { _, ioException -> throw ioException }) { _, _, _ -> Unit }
```

## Status

This HTTP client library is very small.  It's mainly just syntax over existing APIs so there is little to validate.  That said, it should be considered experimental.

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
