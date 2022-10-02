package com.github.vacika.varman.model

open class EnvironmentResponse(
        val page: Int?,
        val values: List<Environments>,
        val size: Int?,
        val pagelen: Int?
)


class Environments(

        val type: String,
        val name: String,
        val slug: String,
        val rank: Int,
        val environment_type: Environment_Type,
        val deployment_gate_enabled: Boolean,
        val lock: Lock,
        val restrictions: Restrictions,
        val hidden: Boolean,
        val environment_lock_enabled: Boolean,
        val category: Category,
        val uuid: String
)


class Environment_Type(
        type: String,
        name: String,
        rank: Int
)

class Lock(
        type: String,
        name: String,
)


class Restrictions(
        val type: String,
        val admin_only: Boolean
)

class Category(
        val name: String
)
