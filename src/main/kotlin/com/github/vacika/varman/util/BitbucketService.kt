package com.github.vacika.varman.util

import com.github.vacika.varman.constants.URLConstants.Companion.deleteEnvironmentUrl
import com.github.vacika.varman.constants.URLConstants.Companion.deleteVariablesURL
import com.github.vacika.varman.constants.URLConstants.Companion.addOrFetchEnvironmentsUrl
import com.github.vacika.varman.constants.URLConstants.Companion.fetchUserInfoUrl
import com.github.vacika.varman.constants.URLConstants.Companion.addOrFetchVariablesURL
import com.github.vacika.varman.model.EnvironmentResponse
import com.github.vacika.varman.model.EnvironmentVariableResponse
import com.github.vacika.varman.util.HTTPHelper.Companion.buildAuthHeader
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*

class BitbucketService {

    companion object {
        private val client = OkHttpClient()
        private val gson = Gson()
        fun signInToBitbucket(username: String, password: String) {
            val request: Request = Request.Builder()
                    .get()
                    .url(fetchUserInfoUrl)
                    .headers(buildAuthHeader(username, password))
                    .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Unexpected code$response")
            }
            CredentialManager.storeBitbucketCredentials(username, password)
        }

        fun fetchEnvironments(): EnvironmentResponse {
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val request = Request.Builder()
                    .get()
                    .url(String.format(addOrFetchEnvironmentsUrl, projectConfig.first, projectConfig.second))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (!responseBody.isSuccessful) {
                throw IOException("Unexpected code ${responseBody.body}")
            }

            val environmentResponse = gson.fromJson(responseBody.body!!.string(), EnvironmentResponse::class.java)
            println("Environment list fetched. List: ${environmentResponse.values.joinToString { it.name }}")
            return environmentResponse
        }

        /**
         * Method for validating project configuration and if valid, then storing it as sensitive data (encrypted storage).
         * Returns EnvironmentResponse because we use the LIST ENVIRONMENTS request to test if the workspace/repository are valid, i.e. 2 flies in one hit.
         */
        fun saveProjectConfiguration(workspace: String, repository: String): EnvironmentResponse {
            println("Saving project configuration started")
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val request = Request.Builder()
                    .get()
                    .url(String.format(addOrFetchEnvironmentsUrl, workspace, repository))
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                CredentialManager.storeProjectConfig(workspace, repository)
                val environmentResponse = gson.fromJson(response.body!!.string(), EnvironmentResponse::class.java)
                println("Project Configuration saved")
                return environmentResponse
            }
        }

        fun fetchEnvironmentVariables(environmentUUID: String): EnvironmentVariableResponse {
            println("Fetching environment variables started")
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val request = Request.Builder()
                    .get()
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .url(String.format(addOrFetchVariablesURL, projectConfig.first, projectConfig.second, environmentUUID))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (!responseBody.isSuccessful) {
                throw IOException("Unexpected code ${responseBody.body}")
            }
            return gson.fromJson(responseBody.body!!.string(), EnvironmentVariableResponse::class.java).also { response ->
                println("Environment Variables fetched ${response.values.joinToString { it.key.plus(":").plus(it.value) }}")
            }
        }

        fun deleteEnvironmentVariable(environmentUUID: String, variableUUID: String) {
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val request = Request.Builder()
                    .delete()
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .url(String.format(deleteVariablesURL, projectConfig.first, projectConfig.second, environmentUUID, variableUUID))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (responseBody.code != 204) {
                throw IOException("Unexpected code ${responseBody.body}")
            }
            println("Environment Variable deleted successfully")
        }

        fun addEnvironment(environmentName: String, type: String) {
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val request = Request.Builder()
                    .post(FormBody.Builder()
                            .add("uuid", UUID.randomUUID().toString())
                            .add("name", environmentName)
                            .add("type", type)
                            .build())
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .url(String.format(addOrFetchEnvironmentsUrl, projectConfig.first, projectConfig.second))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (responseBody.code != 201) {
                throw IOException("Unexpected code ${responseBody.body}")
            }
            println("Environment added successfully")
        }

        fun addVariable(key: String, value: String, type: String, secured: Boolean, environmentUUID: String) {
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val request = Request.Builder()
                    .post(FormBody.Builder()
                            .add("uuid", UUID.randomUUID().toString())
                            .add("key", key)
                            .add("value", value)
                            .add("secured", secured.toString())
                            .add("type", type)
                            .build())
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .url(String.format(addOrFetchVariablesURL, projectConfig.first, projectConfig.second, environmentUUID))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (responseBody.code != 201) {
                throw IOException("Unexpected code ${responseBody.body}")
            }
            println("Variable added successfully")
        }

        fun deleteEnvironment(environmentUUID: String) {
            val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
            val projectConfig = CredentialManager.retrieveProjectConfig()
            val request = Request.Builder()
                    .delete(null)
                    .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                    .url(String.format(deleteEnvironmentUrl, projectConfig.first, projectConfig.second, environmentUUID))
                    .build()
            val responseBody = client.newCall(request).execute()
            if (responseBody.code != 204) {
                throw IOException("Unexpected code ${responseBody.body}")
            }
            println("Environment deleted successfully")
        }
    }
}