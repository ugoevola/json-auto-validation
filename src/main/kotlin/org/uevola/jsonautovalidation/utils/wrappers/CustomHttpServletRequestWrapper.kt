package org.uevola.jsonautovalidation.utils.wrappers

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.stream.Collectors

/**
 * nI order to validate the request before it is deserialized, it is necessary to encapsulate the Body in an
 * HttpServletRequestWrapper used in a RequestFilter.
 * Therefore, the body can be read multiple times, at least one time for the validation, and one time for the deserialization.
 */
class CustomHttpServletRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {

    private val requestBody: String?

    init {
        requestBody = try {
            val inputStream = request.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.lines().collect(Collectors.joining(System.lineSeparator()))
        } catch (e: IOException) {
            // Handle exception
            null
        }
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

