package com.github.vacika.varman.model

open class EnvironmentVariableResponse(
        val page: Int,
        val size: Int,
        val pagelen: Int,
        val values: List<BitbucketVariable>
)
