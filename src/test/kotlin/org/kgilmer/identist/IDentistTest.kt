package org.kgilmer.identist

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.PrintWriter
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class IDentistTest {

    @Test
    fun testHttpGet() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("GET called", request!!.method == "GET")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        val logger = PrintWriter(System.out)
        url.httpGet(mapOf("content-type" to "application/json"), logger = logger) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
        logger.flush()
    }

    @Test
    fun testHttpPostNoBody() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "POST")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpPost(mapOf("content-type" to "application/json")) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpPost() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "POST")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")
                assertTrue("Request body is as expected", request.inputStream.bufferedReader().readText() == "test")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")
        val logger = PrintWriter(System.out)

        url.httpPost(
            headers = mapOf("content-type" to "application/json"),
            body = "test".byteInputStream(),
            logger = logger
        ) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
        logger.flush()
    }

    @Test
    fun testHttpPostLambda() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "POST")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")
                assertTrue("Request body is as expected", request.inputStream.bufferedReader().readText() == "test")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpPost(
            headers = mapOf("content-type" to "application/json"),
            body = "test".byteInputStream()
        ) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpPutNoBody() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "PUT")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpPut(headers = mapOf("content-type" to "application/json")) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpPut() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "PUT")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")
                assertTrue("Request body is as expected", request.inputStream.bufferedReader().readText() == "test")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpPut(
            headers = mapOf("content-type" to "application/json"),
            body = "test".byteInputStream()
        ) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpPutLambda() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("POST called", request!!.method == "PUT")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")
                assertTrue("Request body is as expected", request.inputStream.bufferedReader().readText() == "test")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpPut(
            headers = mapOf("content-type" to "application/json"),
            body = "test".byteInputStream()
        ) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpDelete() {
        val jettyServer =
            startServer(6080) { s: String?, request: Request?, _: HttpServletRequest?, httpServletResponse: HttpServletResponse? ->
                assertTrue("path is as expected", s == "/")
                assertTrue("DELETE called", request!!.method == "DELETE")
                assertTrue(
                    "HTTP headers contain content-type",
                    request.headerNames.toList().map { header -> header.toLowerCase() }.contains("content-type")
                )
                assertTrue("Content-Type has expected value.", request.getHeader("Content-Type") == "application/json")

                httpServletResponse!!.status = HttpServletResponse.SC_OK
                httpServletResponse.writer.println("OK")
                request.isHandled = true
            }

        val url = URL("http://localhost:6080/")

        url.httpDelete(headers = mapOf("content-type" to "application/json")) { statusCode, headers, body ->
            assertTrue("success response code", statusCode == 200)
            assertTrue("has body", body != null)
            assertTrue("has headers", headers.isNotEmpty())
            assertTrue("expected response body", body!!.bufferedReader().readText().trim() == "OK")
        }

        jettyServer.stop()
    }

    @Test
    fun testHttpErrorDoesNotThrowException() {
        val errorReturningUrl =
            URL("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=<API KEY>&artist=bob marley&track=roots&format=json")

        errorReturningUrl.httpGet { statusCode, _, _ ->
            assertTrue("HTTP response is client error.", statusCode == 403)
        }
    }

    private fun startServer(
        port: Int,
        handler: (target: String?, baseRequest: Request?, request: HttpServletRequest?, response: HttpServletResponse?) -> Unit
    ): Server {
        val server = Server(port)
        server.handler = object : AbstractHandler() {
            override fun handle(
                target: String?,
                baseRequest: Request?,
                request: HttpServletRequest?,
                response: HttpServletResponse?
            ) {
                handler(target, baseRequest, request, response)
            }
        }

        server.start()

        return server
    }
}