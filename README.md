# I Dentist

A zero-dependency, HttpURLConnection-based HTTP client in Kotlin for basic use cases.

## Features

* No dependencies, utilizes Java's URL and HttpURLConnection classes.
* Some tests are present.
* Did I mention there are no dependencies?
* MIT Licensed.
* Implemented entirely as extension functions.
* Less than 50 LoC.

## Examples

### HTTP Get

```kotlin
val (status, headers, body) = URL("http://somewebsite.com/somepath").httpGet(mapOf("some-header" to "myval"))
```

### HTTP Post

```kotlin
val (status, headers, body) = URL("http://somewebsite.com/somepath").httpPost() { "{}".byteInputStream() }
```