package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.EnvironmentVariablesAdditionDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class EnvironmentVariablesAddingAction(): AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val dialog = EnvironmentVariablesAdditionDialog()
        val dialogResult = dialog.showAndGet()
        if(dialogResult){
        }
    }
}