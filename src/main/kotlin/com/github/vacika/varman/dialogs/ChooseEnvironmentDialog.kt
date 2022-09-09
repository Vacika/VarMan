package com.github.vacika.varman.dialogs

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.DropDownLink
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBRadioButton
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.event.ChangeListener

class ChooseEnvironmentDialog() : DialogWrapper(true) {
    val panel = JPanel(GridBagLayout())
    var selectedEnv = ""

    init {
        init()
        title = "Environment Variables"
    }

    override fun createCenterPanel(): JComponent? {
        val gb = GridBag()
                .setDefaultInsets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP)
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)


        panel.preferredSize = Dimension(400, 200)

        val environments = fetchEnvironments()
        environments.forEach {
            panel.add(
                    radioButton(it),
                    gb.nextLine().next().weightx(0.8)
            )
        }
        return panel
    }

    private fun radioButton(text: String): JRadioButton {
        val radioButton = JRadioButton(text)
        radioButton.addChangeListener({
            if (radioButton.isSelected) {
                selectedEnv = radioButton.text;
            }
        })
        return radioButton
    }

    private fun fetchEnvironments(): List<String> {
        return listOf("Dev", "Pre_Dev", "Prod", "Pre_Prod")
        TODO("Not yet implemented")
    }

    private fun label(text: String): JComponent {

        val label = JBLabel(text)
        label.componentStyle = UIUtil.ComponentStyle.SMALL
        label.fontColor = UIUtil.FontColor.BRIGHTER
        label.border = JBUI.Borders.empty(0, 5, 2, 0)
        return label
    }
}