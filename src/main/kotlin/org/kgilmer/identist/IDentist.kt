package org.kgilmer.identist

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

fun <T> URL.httpGet(headers: Map<String, String> = mapOf(), action: (Int, Map<String, List<String>>, InputStream?) -> T): T =
        this.http(headers, null, { }, action)

fun <T> URL.httpPost(headers: Map<String, String> = mapOf(), body: InputStream? = null, action: (Int, Map<String, List<String>>, InputStream?) -> T): T =
        this.http(headers, body, { connection ->
            connection.doOutput = true
            connection.requestMethod = "POST"
        }, action)

fun <T> URL.httpPut(headers: Map<String, String> = mapOf(), body: InputStream? = null, action: (Int, Map<String, List<String>>, InputStream?) -> T): T =
        this.http(headers, body, { connection ->
            connection.doOutput = true
            connection.requestMethod = "PUT"
        }, action)

fun <T> URL.httpDelete(headers: Map<String, String> = mapOf(), action: (Int, Map<String, List<String>>, InputStream?) -> T): T =
        this.http(headers, null, { connection ->
            connection.requestMethod = "DELETE"
            connection.doOutput = true
        }, action)

private fun <T> URL.http(headers: Map<String, String> = mapOf(), body: InputStream? = null, connectionVisitor: (HttpURLConnection) -> Unit, action: (Int, Map<String, List<String>>, InputStream?) -> T): T {
    val connection = this.openConnection() as HttpURLConnection
    connectionVisitor(connection)

    headers.forEach { t, u -> connection.setRequestProperty(t, u) }

    body?.apply { this.copyTo(connection.outputStream) }

    try {
        val returnValue = action(connection.responseCode, connection.headerFields, connection.inputStream)
        connection.disconnect()
        return returnValue
    } catch (e: IOException) {
        connection.disconnect()
        throw e
    }
}