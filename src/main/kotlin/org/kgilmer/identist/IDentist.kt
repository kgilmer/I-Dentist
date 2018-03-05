package org.kgilmer.identist

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun URL.httpGet(headers: Map<String, String> = mapOf()): Triple<Int, Map<String, List<String>>, InputStream?> =
        this.http(headers, null) { }

fun URL.httpPost(headers: Map<String, String> = mapOf(), body: InputStream? = null): Triple<Int, Map<String, List<String>>, InputStream?> =
        this.http(headers, body) { connection ->
            connection.doOutput = true
            connection.requestMethod = "POST"
        }

fun URL.httpPost(headers: Map<String, String> = mapOf(), body: () -> InputStream): Triple<Int, Map<String, List<String>>, InputStream?> =
    this.httpPost(headers, body.invoke())

fun URL.httpPut(headers: Map<String, String> = mapOf(), body: InputStream? = null): Triple<Int, Map<String, List<String>>, InputStream?> =
        this.http(headers, body) { connection ->
            connection.doOutput = true
            connection.requestMethod = "PUT"
        }

fun URL.httpPut(headers: Map<String, String> = mapOf(), body: () -> InputStream): Triple<Int, Map<String, List<String>>, InputStream?> =
        this.httpPut(headers, body.invoke())

fun URL.httpDelete(headers: Map<String, String> = mapOf()): Triple<Int, Map<String, List<String>>, InputStream?> =
        this.http(headers, null) { connection ->
            connection.requestMethod = "DELETE"
            connection.doOutput = true
        }

private fun URL.http(headers: Map<String, String> = mapOf(), body: InputStream? = null, connectionVisitor: (HttpURLConnection) -> Unit): Triple<Int, Map<String, List<String>>, InputStream?> {
    val connection = this.openConnection() as HttpURLConnection
    connectionVisitor(connection)

    headers.forEach { t, u -> connection.setRequestProperty(t, u) }

    body?.apply { this.copyTo(connection.outputStream) }

    return Triple(connection.responseCode, connection.headerFields, connection.inputStream)
}