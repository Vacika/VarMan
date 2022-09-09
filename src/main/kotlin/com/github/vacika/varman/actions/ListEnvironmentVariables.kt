package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.ChooseEnvironmentDialog
import com.github.vacika.varman.dialogs.EnvironmentVariableListDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ListEnvironmentVariables: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val initialDialog = ChooseEnvironmentDialog()
        if(initialDialog.showAndGet()) {
            val dialog = EnvironmentVariableListDialog(initialDialog.selectedEnv)
        }
    }
}