package com.github.vacika.varman.constants

class URLConstants {
    companion object{
        const val fetchUserInfoUrl = "https://api.bitbucket.org/2.0/user"
        const val addOrFetchEnvironmentsUrl = "https://api.bitbucket.org/2.0/repositories/%s/%s/environments/"
        const val addOrFetchVariablesURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/deployments_config/environments/%s/variables"
        const val deleteVariablesURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/deployments_config/environments/%s/variables/%s"
        const val deleteEnvironmentUrl = "https://api.bitbucket.org/2.0/repositories/%s/%s/environments/%s"

        val columnHeaders = arrayOf("Name", "Value")
    }
}