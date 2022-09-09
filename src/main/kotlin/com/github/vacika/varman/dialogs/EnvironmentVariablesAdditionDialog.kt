package com.github.vacika.varman.dialogs

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import org.bouncycastle.cms.RecipientId.password
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField

class EnvironmentVariablesAdditionDialog: DialogWrapper(true) {
    val panel = JPanel(GridBagLayout())
    val variableName = JTextField()
    val environment = JTextField()
    val value = JTextField()

    init {
        init()
        title = "Variable Addition Dialog"
    }

    override fun createCenterPanel(): JComponent? {
        val gb = GridBag()
            .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
            .setDefaultWeightX(1.0)
            .setDefaultFill(GridBagConstraints.HORIZONTAL)

        panel.preferredSize = Dimension(400, 200)


        panel.add(label("Variable Name"), gb.nextLine().next().weightx(0.2))
        panel.add(variableName, gb.nextLine().next().weightx(0.8))
        panel.add(label("Environment Name"), gb.nextLine().next().weightx(0.2))
        panel.add(environment, gb.nextLine().next().weightx(0.8))
        panel.add(label("Variable Value"), gb.nextLine().next().weightx(0.2))
        panel.add(value, gb.nextLine().next().weightx(0.8))


        return panel
    }

    private fun label(text: String): JComponent {
        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}