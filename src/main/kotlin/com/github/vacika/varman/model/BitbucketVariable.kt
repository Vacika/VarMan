package com.github.vacika.varman.model

class BitbucketVariable(
        val type: String,
        val uuid: String,
        val key: String,
        val value: String?,
        val secured: Boolean
)