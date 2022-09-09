package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.EnvironmentListDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ListEnvironmentsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        EnvironmentListDialog().show()
    }
}