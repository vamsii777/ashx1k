package com.dewonderstruck.pod.feedback

import android.content.Context
import android.content.Intent

/**
 * Created by Vamsi Madduluri on 31-01-2020.
 */
class EasyFeedback(builder: Builder) {
    private val context: Context
    private val emailId: String?
    private val withSystemInfo: Boolean

    class Builder(internal val context: Context) {
        internal var emailId: String? = null
        internal var withSystemInfo = false
        fun withEmail(email: String?): Builder {
            emailId = email
            return this
        }

        fun withSystemInfo(): Builder {
            withSystemInfo = true
            return this
        }

        fun build(): EasyFeedback {
            return EasyFeedback(this)
        }

    }

    fun start() {
        val intent = Intent(context, FeedbackActivity::class.java)
        intent.putExtra("email", emailId)
        intent.putExtra("with_info", withSystemInfo)
        context.startActivity(intent)
    }

    init {
        emailId = builder.emailId
        context = builder.context
        withSystemInfo = builder.withSystemInfo
    }
}