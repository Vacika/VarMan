package com.github.vacika.varman.actions

import com.github.vacika.varman.dialogs.BitBucketAuthenticationDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class BitBucketAuthenticationAction(): AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialog = BitBucketAuthenticationDialog()
        val dialogResult = dialog.showAndGet()
        if(dialogResult){
        }

    }


}

