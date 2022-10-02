package com.github.vacika.varman.test

import com.github.vacika.varman.dialogs.BitBucketAuthenticationDialog
import com.github.vacika.varman.dialogs.BitBucketConfigurationDialog
import com.github.vacika.varman.model.EnvironmentResponse
import com.github.vacika.varman.model.EnvironmentVariableResponse
import com.github.vacika.varman.util.CredentialManager
import com.github.vacika.varman.util.CredentialManager.Companion.storeBitbucketCredentials
import com.github.vacika.varman.util.HTTPHelper.Companion.buildAuthHeader
import com.google.gson.Gson
import com.intellij.openapi.wm.ToolWindow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableModel


open class VarManToolWindow(toolWindow: ToolWindow) {
    lateinit var environmentResponse: EnvironmentResponse
    private var variableResponse: EnvironmentVariableResponse? = null
    var selectedEnvironment = ""
    private val userURL = "https://api.bitbucket.org/2.0/user"
    private val environmentsUrl = "https://api.bitbucket.org/2.0/repositories/%s/%s/environments/"
    private val variablesURL = "https://api.bitbucket.org/2.0/repositories/%s/%s/deployments_config/environments/%s/variables"
    private val gson = Gson()
    private var client = OkHttpClient()
    private var button1: JButton? = null
    private var button2: JButton? = null
    private var button3: JButton? = null
    var table1: JTable? = null
    var scrollPanel1: JScrollPane? = null
    var comboBox1: JComboBox<String>? = null
    open var myContentPanel: JPanel? = null
    private val columnNames = arrayOf("Name", "Value")

    init {
        button2?.addActionListener { e ->
            run {
                val dialog = BitBucketAuthenticationDialog();
                val dialogResult = dialog.showAndGet();
                // if clicked OK
                if (dialogResult) {
                    try {
                        signInToBitbucket(dialog);
                    } catch (ex: IOException) {
                        throw RuntimeException(ex);
                    }
                    // sign in (bitbucket)
                }
            }
        }
        button3?.addActionListener { e ->
            run {
                val dialog = BitBucketConfigurationDialog()
                val dialogResult = dialog.showAndGet()
                // if clicked OK
                if (dialogResult) {
                    checkValidProjectConfig(dialog)
                    // sign in (bitbucket)
                }
            }
        }
        button1?.addActionListener {
            println("SYNC Clicked")
            environmentResponse = fetchEnvironments()
            comboBox1?.model = DefaultComboBoxModel(environmentResponse.values.map { it.name }.toTypedArray())
            if (selectedEnvironment.isEmpty()) {
                selectedEnvironment = environmentResponse.values.first().name
            }
            comboBox1?.selectedItem = selectedEnvironment
            fetchEnvironmentVariables(environmentResponse.values.first { it.name == selectedEnvironment }.uuid)

        }

        comboBox1?.addActionListener {
            println("Environment changed")
            selectedEnvironment = comboBox1?.selectedItem as String
            fetchEnvironmentVariables(environmentResponse.values.first { it.name == selectedEnvironment }.uuid)
        }

    }

    fun createUIComponents() {
        val dm = DefaultTableModel(columnNames, 0)
        table1 = JTable(dm)
        scrollPanel1 = JScrollPane(table1)
    }

    @Throws(IOException::class)
    private fun signInToBitbucket(dialog: BitBucketAuthenticationDialog) {
        val request: Request = Request.Builder()
                .get()
                .url(userURL)
                .headers(buildAuthHeader(dialog.username.text, dialog.password.text))
                .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code$response")
        }
        storeBitbucketCredentials(dialog.username.text, dialog.password.text)
    }

    private fun checkValidProjectConfig(dialog: BitBucketConfigurationDialog) {
        val bitbucketCredentials = CredentialManager.retrieveBitbucketCredentials()
        val request = Request.Builder()
                .get()
                .url(String.format(environmentsUrl, dialog.workspace.text, dialog.repository.text))
                .headers(buildAuthHeader(bitbucketCredentials.first, bitbucketCredentials.second))
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            CredentialManager.storeProjectConfig(dialog.workspace.text, dialog.repository.text)
            environmentResponse = gson.fromJson(response.body!!.string(), EnvironmentResponse::class.java)
            println("environments fetched")
        }
    }

    private fun fetchEnvironments(): EnvironmentResponse {
        val projectConfig = CredentialManager.retrieveProjectConfig()
        val request = Request.Builder()
                .get()
                .url(String.format(environmentsUrl, projectConfig.first, projectConfig.second))
                .build()
        val responseBody = client.newCall(request).execute()
        if (!responseBody.isSuccessful) {
            throw IOException("Unexpected code ${responseBody.body}")
        }

        environmentResponse = gson.fromJson(responseBody.body!!.string(), EnvironmentResponse::class.java)
        println("environments fetched")
        return environmentResponse
    }

    private fun fetchEnvironmentVariables(environmentUUID: String): EnvironmentVariableResponse? {
        val projectConfig = CredentialManager.retrieveProjectConfig()
        val request = Request.Builder()
                .get()
                .url(String.format(variablesURL, projectConfig.first, projectConfig.second, environmentUUID))
                .build()
        val responseBody = client.newCall(request).execute()
        if (!responseBody.isSuccessful) {
            throw IOException("Unexpected code ${responseBody.body}")
        }
        variableResponse = gson.fromJson(responseBody.body!!.string(), EnvironmentVariableResponse::class.java)

        val newDM = DefaultTableModel(columnNames, 0)
        variableResponse!!.values.forEach { newDM.addRow(arrayOf(it.key, it.value ?: "**SECURED**")) }
        table1?.model = newDM
        newDM.fireTableDataChanged()
        println("Variables fetched, recreating panel again")
        return variableResponse
    }

    open fun getContentPanel(): JPanel? {
        return myContentPanel
    }
}