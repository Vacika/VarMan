package com.github.vacika.varman.dialogs

import com.github.vacika.varman.interceptor.BasicAuthInterceptor
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

class EnvironmentListDialog : DialogWrapper(true) {

    val baseURL = "https://api.bitbucket.org/2.0/repositories/hellotoday/ht-task-manager/environments/"
    val panel = JPanel(GridBagLayout())
    val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Vasko Jovanoski",
            "ATBB84xrHzzFJyRTPDGuNvQFThdg83A82A67"))
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
        return panel
    }

    private fun fetchEnvironments(): List<String> {
        val request = Request.Builder().url(this.baseURL).build()
        val call = client.newCall(request)
        val response = call.execute()
        return listOf()
    }

    private fun label(text: String): JComponent {
        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}