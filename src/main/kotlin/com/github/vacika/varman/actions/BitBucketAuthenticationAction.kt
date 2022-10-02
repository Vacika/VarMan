package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.BitBucketAuthenticationDialog
import com.github.vacika.varman.util.CredentialManager.Companion.storeBitbucketCredentials
import com.github.vacika.varman.util.HTTPHelper.Companion.buildAuthHeader
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class BitBucketAuthenticationAction() : AnAction() {
    val baseURL = "https://api.bitbucket.org/2.0/user"
    private val client = OkHttpClient()

    override fun actionPerformed(e: AnActionEvent) {
        val dialog = BitBucketAuthenticationDialog()
        val dialogResult = dialog.showAndGet()
        // if clicked OK
        if (dialogResult) {
            signInToBitbucket(dialog)
            // sign in (bitbucket)
        }
    }

    private fun signInToBitbucket(dialog: BitBucketAuthenticationDialog) {
        val request = Request.Builder()
                .get()
                .url(baseURL)
                .headers(buildAuthHeader(dialog.username.text, dialog.password.text))
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            storeBitbucketCredentials(dialog.username.text, dialog.password.text)
            println(response.body!!.string())
        }
        println("Dialog Result:" + dialog.username.text + ":" + dialog.password.text)
    }
}

