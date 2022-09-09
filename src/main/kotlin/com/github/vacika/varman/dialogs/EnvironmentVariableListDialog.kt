package com.github.vacika.varman.dialogs

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

class EnvironmentVariableListDialog(val environment: String) : DialogWrapper(true) {
    val panel = JPanel(GridBagLayout())

    init {
        init()
        title = "Environment Variables"
    }

    override fun createCenterPanel(): JComponent? {
        val gb = GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)

        val envVars = fetchEnvironmentVariables(environment)
        panel.preferredSize = Dimension(400, 200)

        envVars.forEach { envVar ->
            panel.add(label(envVar.key), gb.nextLine().next().weightx(0.8))
            panel.add(label(envVar.value), gb.nextLine().next().weightx(0.2))
        }
        return panel
    }

    private fun fetchEnvironmentVariables(environment: String): Map<String, String> {
        val map = mapOf<String, String>("test" to "test")
        return map
        //TODO: Implement
    }

    private fun label(text: String): JComponent {
        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}