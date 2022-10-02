package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.BitBucketConfigurationDialog
import com.github.vacika.varman.util.CredentialManager.Companion.retrieveBitbucketCredentials
import com.github.vacika.varman.util.CredentialManager.Companion.storeProjectConfig
import com.github.vacika.varman.util.HTTPHelper.Companion.buildAuthHeader
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class BitBucketConfigurationAction() : AnAction() {
    private val baseURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/environments/"
    private val client = OkHttpClient()

    override fun actionPerformed(e: AnActionEvent) {
        val dialog = BitBucketConfigurationDialog()
        val dialogResult = dialog.showAndGet()
        // if clicked OK
        if (dialogResult) {
            fetchEnvironments(dialog)
            // sign in (bitbucket)
        }
    }

    private fun fetchEnvironments(dialog: BitBucketConfigurationDialog) {
        val bitbucketCredentials = retrieveBitbucketCredentials()
        val request = Request.Builder()
                .get()
                .url(String.format(baseURL, dialog.workspace.text, dialog.repository.text))
                .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            storeProjectConfig(dialog.workspace.text, dialog.repository.text)
            println(response.body!!.string())
        }
    }
}

