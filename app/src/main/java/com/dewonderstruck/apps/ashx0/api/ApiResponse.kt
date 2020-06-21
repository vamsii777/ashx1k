package com.dewonderstruck.apps.ashx0.api

import androidx.collection.ArrayMap
import com.dewonderstruck.apps.ashx0.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T>
</T> */
class ApiResponse<T> {
    private val code: Int
    @JvmField
    val body: T?
    @JvmField
    val errorMessage: String?
    private val links: Map<String, String>

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
        links = emptyMap()
        Utils.psLog("API Response Error : $errorMessage")
    }

    constructor(response: Response<T>) {
        code = response.code()
        Utils.psLog("URL : " + response.raw().request().url())
        if (response.isSuccessful) {
            Utils.psLog("ApiResponse Successful.")
            body = response.body()
            errorMessage = null
        } else {
            Utils.psLog("ApiResponse Something wrong.")
            var message: String? = null
            try {
                val responseBody = response.errorBody()
                if (responseBody != null) {
                    message = responseBody.string()
                    try {
                        val jObjError = JSONObject(message)
                        Utils.psLog("API Error Response : " + jObjError.getString("message"))
                        message = jObjError.getString("message")
                    } catch (e: JSONException) {
                        Utils.psErrorLog("JSON Parsing error.", e)
                    }
                }
            } catch (ne: NullPointerException) {
                Utils.psErrorLog("Null Pointer Exception.", ne)
            } catch (ignored: IOException) {
                Utils.psErrorLog("error while parsing response", ignored)
            }
            if (message == null || message.trim { it <= ' ' }.length == 0) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
        val linkHeader = response.headers()["link"]
        if (linkHeader == null) {
            links = emptyMap()
        } else {
            links = ArrayMap()
            val matcher = LINK_PATTERN.matcher(linkHeader)
            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1))
                }
            }
        }
    }

    val isSuccessful: Boolean
        get() = code >= 200 && code < 300

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(next)
            return if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else try {
                matcher.group(1).toInt()
            } catch (ex: NumberFormatException) {
                Utils.psLog("cannot parse next page from %s")
                null
            }
        }

    companion object {
        private val LINK_PATTERN = Pattern
                .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"
    }
}