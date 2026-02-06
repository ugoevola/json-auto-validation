package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.HttpStatus
import org.uevola.jsonautovalidation.runtime.common.utils.ExceptionUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * To validate the request before it is deserialized, it is necessary to encapsulate the Body in an
 * HttpServletRequestWrapper used in a RequestFilter.
 * Therefore, the body can be read multiple times, at least one time for the validation, and one time for the deserialization.
 */
internal class CustomHttpServletRequestWrapper(
    request: HttpServletRequest,
    maxJsonSize: Long
) : HttpServletRequestWrapper(request) {

    private val requestBody: String? = try {
        val inputStream = request.inputStream
        val bytes = mutableListOf<Byte>()
        var count = 0L
        var byte = inputStream.read()
        while (byte != -1) {
            count++
            if (count > maxJsonSize) {
                throw ExceptionUtils.httpClientErrorException(
                    "Payload Too Large: JSON body exceeds maximum size of $maxJsonSize bytes",
                    HttpStatus.PAYLOAD_TOO_LARGE
                )
            }
            bytes.add(byte.toByte())
            byte = inputStream.read()
        }
        String(bytes.toByteArray(), Charsets.UTF_8)
    } catch (_: IOException) {
        null
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(inputStream))
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        val byteArray = requestBody?.toByteArray() ?: ByteArray(0)
        return CustomServletInputStream(byteArray)
    }

    private class CustomServletInputStream(private val byteArray: ByteArray) : ServletInputStream() {
        private var position = 0

        override fun read(): Int {
            return if (position >= byteArray.size) -1 else byteArray[position++].toInt()
        }

        override fun isFinished(): Boolean {
            return this.isFinished
        }

        override fun isReady(): Boolean {
            return this.isReady
        }

        override fun setReadListener(readListener: ReadListener) {
            this.setReadListener(readListener)
        }
    }
}