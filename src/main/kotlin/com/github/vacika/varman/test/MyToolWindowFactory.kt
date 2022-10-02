package com.github.vacika.varman.test

import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;

class MyToolWindowFactory : ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(@NotNull project: Project, @NotNull toolWindow: ToolWindow) {
        val myToolWindow = VarManToolWindow(toolWindow)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content: Content = contentFactory.createContent(myToolWindow.getContentPanel(), "", false)
        toolWindow.getContentManager().addContent(content)
    }
}