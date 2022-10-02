package com.github.vacika.varman.dialogs

import com.github.vacika.varman.util.CredentialManager.Companion.retrieveProjectConfig
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

class BitBucketConfigurationDialog : DialogWrapper(true) {
    private val panel = JPanel(GridBagLayout())
    val workspace = JTextField()
    val repository = JTextField()

    init {
        init()
        title = "Project Configuration"
    }

    override fun createCenterPanel(): JComponent {
        try {
            val projectConfig = retrieveProjectConfig()
            workspace.text = projectConfig.first
            repository.text = projectConfig.second
        } catch (_: RuntimeException) {
        }
        val gb = GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)

        panel.preferredSize = Dimension(400, 120)

        panel.add(createTextLabel("Workspace (Case Sensitive)"), gb.nextLine().next().weightx(0.2))
        panel.add(workspace, gb.nextLine().next().weightx(0.8))
        panel.add(createTextLabel("Repository Name (Case Sensitive)"), gb.nextLine().next().weightx(0.2))
        panel.add(repository, gb.nextLine().next().weightx(0.8))
        return panel
    }

    private fun createTextLabel(text: String): JComponent {
        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}