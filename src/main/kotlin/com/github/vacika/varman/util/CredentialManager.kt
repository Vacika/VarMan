package com.github.vacika.varman.util

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.Credentials
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe

class CredentialManager {
    companion object {
        private fun createCredentialAttributes(key: String): CredentialAttributes {
            return CredentialAttributes(
                    generateServiceName("VarMan", key)
            )
        }

        fun storeBitbucketCredentials(username: String, password: String) {
            val credentialAttributes = createCredentialAttributes("BitBucket")
            val credentials = Credentials(username, password)
            PasswordSafe.instance.set(credentialAttributes, credentials)
        }

        fun retrieveBitbucketCredentials(): Pair<String, String> {
            val credentialAttributes = createCredentialAttributes("BitBucket")
            val credentials = PasswordSafe.instance.get(credentialAttributes)
            if (credentials?.userName != null && credentials.password != null) {
                return Pair(credentials.userName!!, credentials.getPasswordAsString()!!)
            } else {
                throw RuntimeException("Couldn't retrieve Bitbucket credentials from secret storage")
            }
        }

        fun storeProjectConfig(workspace: String, repository: String) {
            val credentialAttributes = createCredentialAttributes("ProjectConfig")
            val credentials = Credentials(workspace, repository)
            PasswordSafe.instance.set(credentialAttributes, credentials)
        }

        fun retrieveProjectConfig(): Pair<String, String> {
            val credentialAttributes = createCredentialAttributes("ProjectConfig")
            val credentials = PasswordSafe.instance.get(credentialAttributes)
            if (credentials?.userName != null && credentials.password != null) {
                return Pair(credentials.userName!!, credentials.getPasswordAsString()!!)
            } else {
                throw RuntimeException("Couldn't retrieve Project Configuration from secret storage")
            }
        }
    }
}