package com.github.vacika.varman.services

import com.intellij.openapi.project.Project
import com.github.vacika.varman.MyBundle
import com.intellij.ide.actions.runAnything.RunAnythingContext.BrowseRecentDirectoryContext.label
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.AbstractLayout
import com.intellij.util.ui.GridBag
import com.intellij.util.ui.JBUI

import com.intellij.util.ui.UI.PanelFactory.panel
import com.intellij.util.ui.UIUtil
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}