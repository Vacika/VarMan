package com.github.vacika.varman.dialogs

import com.github.vacika.varman.interceptor.BasicAuthInterceptor
import com.github.vacika.varman.model.EnvironmentResponse
import com.github.vacika.varman.model.EnvironmentVariableResponse
import com.github.vacika.varman.util.CredentialManager.Companion.retrieveBitbucketCredentials
import com.github.vacika.varman.util.CredentialManager.Companion.retrieveProjectConfig
import com.google.gson.Gson
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.DropDownLink
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.containers.toArray
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.io.IOException
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTable


class EnvironmentListDialog : DialogWrapper(true) {
    val gson = Gson()
    lateinit var environmentResponse: EnvironmentResponse
    var variableResponse: EnvironmentVariableResponse? = null
    var selectedEnvironment = ""
    val environmentsURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/environments/"
    val variablesURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/deployments_config/environments/%s/variables"
    val panel = JPanel(GridBagLayout())
    val credentials = retrieveBitbucketCredentials()
    val projectConfig = retrieveProjectConfig()
    val client = OkHttpClient.Builder().addInterceptor(
            BasicAuthInterceptor(
                    credentials.first,
                    credentials.second
            )
    ).build()

    init {
        init()
        title = "Environments"

    }

    override fun createCenterPanel(): JComponent {
        val gb = GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)

        val environments = fetchEnvironments()
        panel.preferredSize = Dimension(400, 200)
        panel.add(label("Select Environment: $selectedEnvironment", "selEnvLabel"), gb.nextLine().next().weightx(0.5))
        panel.add(DropDownLink(selectedEnvironment, environments) { item -> onEnvSelect(item) }, gb.nextLine().next().weightx(0.5))
        panel.add(createTable(), gb.nextLine().next().weightx(0.5))
        return panel
    }

    private fun onEnvSelect(environment: String) {
        selectedEnvironment = environment
        val environmentUUID = environmentResponse.values.first { it.name == environment }.uuid
        fetchEnvironmentVariables(environmentUUID)
        println("Environment selected: $environment")
    }

    private fun fetchEnvironmentVariables(environmentUUID: String) {
        val request = Request.Builder()
                .get()
                .url(String.format(variablesURL, projectConfig.first, projectConfig.second, environmentUUID))
                .build()
        val responseBody = client.newCall(request).execute()
        if (!responseBody.isSuccessful) {
            throw IOException("Unexpected code ${responseBody.body}")
        }
        variableResponse = gson.fromJson(responseBody.body!!.string(), EnvironmentVariableResponse::class.java)
        println("Variables fetched, recreating panel again")
//        createCenterPanel()
    }

    private fun fetchEnvironments(): List<String> {
        val request = Request.Builder()
                .get()
                .url(String.format(environmentsURL, projectConfig.first, projectConfig.second))
                .build()
        val responseBody = client.newCall(request).execute()
        if (!responseBody.isSuccessful) {
            throw IOException("Unexpected code ${responseBody.body}")
        }

        environmentResponse = gson.fromJson(responseBody.body!!.string(), EnvironmentResponse::class.java)
        return environmentResponse.values.map { it.name }
    }

    private fun createTable(): JTable {
        val columnNames = arrayOf("Key", "Value")

        val data = if (variableResponse == null) arrayOf() else variableResponse!!.values.map {
            arrayOf(it.key, it.value ?: "**SECURED**")
        }.toTypedArray()
        val table = JTable(data, columnNames)
        table.name = "VariableTable"
        table.fillsViewportHeight = true
        return table
    }

    private fun label(text: String, name: String? = null): JComponent {
        val label = JBLabel(text)
        label.name = name
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}