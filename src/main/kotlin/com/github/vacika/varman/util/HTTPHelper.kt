package com.github.vacika.varman.util

import okhttp3.Headers
import java.util.*

class HTTPHelper {
    companion object {
        fun buildAuthHeader(username: String, password: String): Headers {
            return Headers.headersOf("Authorization", "Basic " + (toBase64("$username:$password")))
        }

        private fun toBase64(content: String): String? {
            return Base64.getEncoder().encodeToString(content.toByteArray())
        }
    }
}