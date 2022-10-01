package com.github.vacika.varman.dialogs

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.vacika.varman.interceptor.BasicAuthInterceptor
import com.google.gson.Gson
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTable


class EnvironmentListDialog : DialogWrapper(true) {

    val baseURL = "https://api.bitbucket.org/2.0/repositories/N47/ht-task-manager/environments/"
    val panel = JPanel(GridBagLayout())
    val client = OkHttpClient.Builder().addInterceptor(
        BasicAuthInterceptor(
            "vasko-jovanoski",
            "ATBBMXXmXc7VnaP2Q46VLwvpDsrZ2313041C"
        )
    )
        .build()

    init {
        init()
        title = "Environments"

    }

    override fun createCenterPanel(): JComponent? {
        val gb = GridBag()
            .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
            .setDefaultWeightX(1.0)
            .setDefaultFill(GridBagConstraints.HORIZONTAL)

        val environments = fetchEnvironments()
        panel.preferredSize = Dimension(400, 200)

        environments.forEach { env ->
            panel.add(label(env), gb.nextLine().next().weightx(0.5))
        }

        panel.add(createTable(), gb.nextLine().next().weightx(0.2))
        return panel
    }

    private fun fetchEnvironments(): List<String> {
        val mapper = jacksonObjectMapper()
        val gson = Gson()
        val request = Request.Builder().url(this.baseURL).build()
        val call = client.newCall(request)
        val response = call.execute().body
//        val json = JSONObject(response!!)
        gson.fromJson(response?.string(), Environment::class.java)


        return listOf()
    }

    fun createTable(): JTable {
        val columnNames = arrayOf("Key", "Value")
        val data = arrayOf(
            arrayOf<Any>("DEPLOYMENT_ENV", "DEV"),
            arrayOf<Any>("K8S_CLUSTER_NAME", "n47-gke-cluster-dev"),
            arrayOf<Any>("K8S_CLUSTER_ZONE", "europe-west3-a"),
            arrayOf("SLACK_TOKEN", "12tsk2s51sknsbdga0")
        )
        val table = JTable(data, columnNames)
        table.fillsViewportHeight = true
        return table
    }

    private fun label(text: String): JComponent {
        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}