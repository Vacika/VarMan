package com.github.vacika.varman.dialogs

import com.github.vacika.varman.util.CredentialManager
import com.github.vacika.varman.util.CredentialManager.Companion.retrieveBitbucketCredentials
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
import javax.swing.JPasswordField
import javax.swing.JTextField

class BitBucketAuthenticationDialog : DialogWrapper(true) {
    val panel = JPanel(GridBagLayout())
    val username = JTextField()
    val password = JPasswordField()
    val projectName = JTextField()
    val repositoryName = JTextField()

    init {
        init()
        title = "Project Configuration"
    }

    override fun createCenterPanel(): JComponent {
        val gb = GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)

        panel.preferredSize = Dimension(400, 200)
        try {
            val credentials = retrieveBitbucketCredentials()
            val loggedUser = credentials.first
            panel.add(createTextLabel("You are already logged in as $loggedUser."), gb.nextLine().next().weightx(0.2))
            panel.add(createTextLabel("If you wish to switch users, just enter the new credentials below."), gb.nextLine().next().weightx(0.2))
        } catch (_: RuntimeException) {

        }
        panel.add(createTextLabel("Username (not email)"), gb.nextLine().next().weightx(0.2))
        panel.add(username, gb.nextLine().next().weightx(0.8))
        panel.add(createTextLabel("App Password"), gb.nextLine().next().weightx(0.2))
        panel.add(password, gb.nextLine().next().weightx(0.8))
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