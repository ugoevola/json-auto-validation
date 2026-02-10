package org.uevola.jsonautovalidation.runtime.servlet.web

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.HttpStatus
import org.springframework.web.util.WebUtils
import org.uevola.jsonautovalidation.runtime.common.utils.ExceptionUtils
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * To validate the request before it is deserialized, it is necessary to encapsulate the Body in an
 * HttpServletRequestWrapper used in a RequestFilter.
 * Therefore, the body can be read multiple times, at least one time for the validation, and one time for the deserialization.
 */
internal class JsonAutoValidationRequestWrapper(
    request: HttpServletRequest,
    private val maxJsonSize: Long
) : HttpServletRequestWrapper(request) {

    private var cachedContent: ByteArrayOutputStream? = null

    @Throws(IOException::class)
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(inputStream, characterEncoding ?: "UTF-8"))
    }

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        if (this.cachedContent == null) {
            this.cachedContent = ByteArrayOutputStream()
            val isOriginal = super.getInputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            var totalBytes = 0L
            while (isOriginal.read(buffer).also { bytesRead = it } != -1) {
                totalBytes += bytesRead
                if (totalBytes > maxJsonSize) {
                    throw ExceptionUtils.httpClientErrorException(
                        "Payload Too Large: JSON body exceeds maximum size of $maxJsonSize bytes",
                        HttpStatus.PAYLOAD_TOO_LARGE
                    )
                }
                this.cachedContent!!.write(buffer, 0, bytesRead)
            }
        }
        return ContentCachingInputStream(this.cachedContent!!.toByteArray())
    }

    override fun getCharacterEncoding(): String {
        return super.getCharacterEncoding() ?: WebUtils.DEFAULT_CHARACTER_ENCODING
    }

    private class ContentCachingInputStream(private val cachedContent: ByteArray) : ServletInputStream() {
        private var index = 0

        override fun read(): Int {
            return if (index < cachedContent.size) {
                cachedContent[index++].toInt() and 0xFF
            } else {
                -1
            }
        }

        override fun isFinished(): Boolean {
            return index >= cachedContent.size
        }

        override fun isReady(): Boolean {
            return true
        }

        override fun setReadListener(readListener: ReadListener) {
            this.setReadListener(readListener)
        }
    }
}