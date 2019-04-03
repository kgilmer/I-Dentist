package org.kgilmer.identist

import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL


fun <T> URL.httpGet(
    headers: Map<String, String> = mapOf(),
    errorHandler: (HttpURLConnection, IOException) -> T = { conn, _ ->
        action(conn.responseCode, conn.headerFields, null)
    },
    logger: PrintWriter? = null,
    action: (Int, Map<String, List<String>>, InputStream?) -> T
): T =
    this.http(
        headers = headers,
        body = null,
        connectionVisitor = { },
        errorHandler = errorHandler,
        logger = logger,
        action = action
    )

fun <T> URL.httpPost(
    headers: Map<String, String> = mapOf(),
    errorHandler: (HttpURLConnection, IOException) -> T = { conn, _ ->
        action(conn.responseCode, conn.headerFields, null)
    },
    logger: PrintWriter? = null,
    body: InputStream? = null,
    action: (Int, Map<String, List<String>>, InputStream?) -> T
): T =
    this.http(
        headers = headers,
        body = body,
        connectionVisitor = { connection ->
            connection.doOutput = true
            connection.requestMethod = "POST"
        },
        errorHandler = errorHandler,
        logger = logger,
        action = action
    )

fun <T> URL.httpPut(
    headers: Map<String, String> = mapOf(),
    errorHandler: (HttpURLConnection, IOException) -> T = { conn, _ ->
        action(conn.responseCode, conn.headerFields, null)
    },
    logger: PrintWriter? = null,
    body: InputStream? = null,
    action: (Int, Map<String, List<String>>, InputStream?) -> T
): T =
    this.http(
        headers = headers,
        body = body,
        connectionVisitor = { connection ->
            connection.doOutput = true
            connection.requestMethod = "PUT"
        }, errorHandler = errorHandler,
        logger = logger,
        action = action
    )

fun <T> URL.httpDelete(
    headers: Map<String, String> = mapOf(),
    errorHandler: (HttpURLConnection, IOException) -> T = { conn, _ ->
        action(conn.responseCode, conn.headerFields, null)
    },
    logger: PrintWriter? = null,
    action: (Int, Map<String, List<String>>, InputStream?) -> T
): T =
    this.http(
        headers = headers,
        body = null,
        connectionVisitor = { connection ->
            connection.requestMethod = "DELETE"
            connection.doOutput = true
        },
        errorHandler = errorHandler,
        logger = logger,
        action = action
    )

private fun <T> URL.http(
    headers: Map<String, String> = mapOf(),
    body: InputStream? = null,
    connectionVisitor: (HttpURLConnection) -> Unit,
    errorHandler: (HttpURLConnection, IOException) -> T,
    logger: PrintWriter? = null,
    action: (Int, Map<String, List<String>>, InputStream?) -> T
): T {
    logger?.println("--> Connecting to $this")
    val connection = this.openConnection() as HttpURLConnection
    connectionVisitor(connection)

    logger?.println("--> Headers: $headers")
    headers.forEach { t, u -> connection.setRequestProperty(t, u) }

    if (body != null && logger != null) {
        val bodyContent = body.readBytes()
        logger.println("--> Body: ${String(bodyContent)}")
        connection.outputStream.write(bodyContent)
    } else {
        body?.apply { this.copyTo(connection.outputStream) }
    }

    return try {
        val returnValue = action(connection.responseCode, connection.headerFields, connection.inputStream)
        connection.disconnect()
        logger?.println("<-- status: ${connection.responseCode} headers: ${connection.headerFields}")
        returnValue
    } catch (e: IOException) {
        connection.disconnect()
        logger?.println("<-- status: ${connection.responseCode} headers: ${connection.headerFields}")
        logger?.flush()
        errorHandler(connection, e)
    }
}
