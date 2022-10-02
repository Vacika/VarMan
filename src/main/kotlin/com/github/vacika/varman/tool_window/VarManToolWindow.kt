package com.github.vacika.varman.tool_window

import com.github.vacika.varman.constants.URLConstants.Companion.columnHeaders
import com.github.vacika.varman.dialogs.BitBucketAuthenticationDialog
import com.github.vacika.varman.dialogs.BitBucketConfigurationDialog
import com.github.vacika.varman.model.BitbucketVariable
import com.github.vacika.varman.model.EnvironmentResponse
import com.github.vacika.varman.model.EnvironmentVariableResponse
import com.github.vacika.varman.util.BitbucketService.Companion.deleteEnvironment
import com.github.vacika.varman.util.BitbucketService.Companion.deleteEnvironmentVariable
import com.github.vacika.varman.util.BitbucketService.Companion.fetchEnvironmentVariables
import com.github.vacika.varman.util.BitbucketService.Companion.fetchEnvironments
import com.github.vacika.varman.util.BitbucketService.Companion.saveProjectConfiguration
import com.github.vacika.varman.util.BitbucketService.Companion.signInToBitbucket
import com.intellij.openapi.wm.ToolWindow
import java.io.IOException
import javax.swing.*
import javax.swing.table.DefaultTableModel


open class VarManToolWindow(toolWindow: ToolWindow) {
    private var selectedVariable: BitbucketVariable? = null
    private var variableResponse: EnvironmentVariableResponse? = null
    private var environmentResponse: EnvironmentResponse? = null
    private var selectedEnvironment = ""
    private var syncButton: JButton? = null
    private var authButton: JButton? = null
    private var projectConfigButton: JButton? = null
    private var addVariableBtn: JButton? = null
    private var deleteVariableBtn: JButton? = null
    private var addEnvironmentBtn: JButton? = null
    private var deleteEnvironmentBtn: JButton? = null
    private var variablesTable: JTable? = null
    private var tableScrollPanel: JScrollPane? = null
    private var environmentDropdown: JComboBox<String>? = null
    open var myContentPanel: JPanel? = null

    init {
        setupEventListeners()
    }

    private fun setupEventListeners() {
        setAuthButtonEventListener()
        setAddVariableBtnEventListener()
        setDeleteVariableBtnEventListener()
        setAddEnvironmentBtnEventListener()
        setDeleteEnvironmentBtnEventListener()
        setProjectConfigButtonListener()
        setSyncButtonProjectListener()
        setEnvironmentDropdownEventListener()
        setTableListener()
    }

    private fun setTableListener() {
        variablesTable?.selectionModel?.addListSelectionListener {
            println("Table row selection event caught")
            try {
                if (variablesTable!!.selectedRow != -1) {
                    val row = variablesTable!!.getValueAt(variablesTable!!.selectedRow, 0)
                    println("Variable Key = $row")
                    selectedVariable = findVariableObjectByKey(row.toString())?.also { deleteVariableBtn?.isEnabled = true }
                } else {
                    deleteVariableBtn?.isEnabled = false
                }
            } catch (e: Exception) {
                println("Something wrong happened while listening for changes in table")
            }
        }
    }

    private fun findVariableObjectByKey(key: String): BitbucketVariable? {
        return variableResponse?.values?.firstOrNull { it.key == key }
    }

    private fun setDeleteEnvironmentBtnEventListener() {
        deleteEnvironmentBtn?.isEnabled = false
        deleteEnvironmentBtn?.addActionListener {
            deleteEnvironment(getSelectedEnvironmentUUID())
            environmentDropdown?.removeAllItems()
            environmentResponse = fetchEnvironments()
            selectedEnvironment = environmentResponse?.values?.firstOrNull()?.name ?: ""
            println("Selected ENVVVVVVVVVVVVV = $selectedEnvironment")
            println("FETCHED ENVS = ${environmentResponse?.values?.joinToString { it.name }}")
            environmentDropdown!!.model = DefaultComboBoxModel(environmentResponse!!.values.map { it.name }.toTypedArray())
            environmentDropdown!!.selectedItem = selectedEnvironment

            if (environmentDropdown?.selectedItem != null) fetchVariablesAndUpdateTable()
        }
    }

    private fun setAddEnvironmentBtnEventListener() {
        addEnvironmentBtn?.isEnabled = false
        addEnvironmentBtn?.addActionListener {
            // add environment here
        }
    }

    private fun setDeleteVariableBtnEventListener() {
        deleteVariableBtn?.isEnabled = false
        deleteVariableBtn?.addActionListener {
            deleteEnvironmentVariable(getSelectedEnvironmentUUID(), selectedVariable!!.uuid)
            fetchVariablesAndUpdateTable()
        }
    }

    private fun setAddVariableBtnEventListener() {
        addVariableBtn?.isEnabled = false
        addVariableBtn?.addActionListener {
            // add variable here
        }
    }

    private fun setEnvironmentDropdownEventListener() {
        environmentDropdown?.addActionListener {
            println("Environment selection changed. Selected Item: ${environmentDropdown?.selectedItem}")
            if (environmentDropdown?.selectedItem != null && environmentDropdown?.selectedItem != "") {
                selectedEnvironment = environmentDropdown?.selectedItem as String
                fetchVariablesAndUpdateTable()
            }
        }
    }

    private fun setSyncButtonProjectListener() {
        syncButton?.addActionListener {
            println("Synchronize Button Clicked")
            environmentResponse = fetchEnvironments()
            enableEnvironmentButtons()
            environmentDropdown?.model = DefaultComboBoxModel(environmentResponse?.values?.map { it.name }?.toTypedArray())
            if (selectedEnvironment.isEmpty()) {
                selectedEnvironment = environmentResponse?.values?.firstOrNull()?.name ?: ""
            }
            environmentDropdown?.selectedItem = selectedEnvironment
            fetchVariablesAndUpdateTable()
        }
    }

    private fun enableEnvironmentButtons() {
        addEnvironmentBtn?.isEnabled = true
        deleteEnvironmentBtn?.isEnabled = environmentResponse!!.values.isNotEmpty()
        addVariableBtn?.isEnabled = true
    }

    private fun setProjectConfigButtonListener() {
        projectConfigButton?.addActionListener {
            println("Project Config button clicked")
            val dialog = BitBucketConfigurationDialog()
            val dialogResult = dialog.showAndGet()
            // if clicked OK
            if (dialogResult) {
                environmentResponse = saveProjectConfiguration(dialog.workspace.text, dialog.repository.text)
            }
        }
    }

    private fun setAuthButtonEventListener() {
        authButton?.addActionListener {
            println("Auth button clicked")
            val dialog = BitBucketAuthenticationDialog()
            val dialogResult = dialog.showAndGet()
            // if clicked OK
            if (dialogResult) {
                try {
                    signInToBitbucket(dialog.username.text, dialog.password.text)
                } catch (ex: IOException) {
                    throw RuntimeException(ex);
                }
            }

        }
    }

    fun createUIComponents() {
        val dm = DefaultTableModel(columnHeaders, 0)
        environmentDropdown = JComboBox(DefaultComboBoxModel(arrayOf()))
        variablesTable = JTable(dm)
        tableScrollPanel = JScrollPane(variablesTable)
    }

    private fun updateVariablesTable(variableResponse: EnvironmentVariableResponse) {
        val newDM = DefaultTableModel(columnHeaders, 0)
        variableResponse.values.forEach { newDM.addRow(arrayOf(it.key, it.value ?: "**SECURED**")) }
        variablesTable?.model = newDM
        newDM.fireTableDataChanged()
        println("Recreating EnvironmentTable now.")
    }

    private fun fetchVariablesAndUpdateTable() {
        variableResponse = fetchEnvironmentVariables(getSelectedEnvironmentUUID())
        updateVariablesTable(variableResponse!!)
    }

    private fun getSelectedEnvironmentUUID(): String {
        return environmentResponse!!.values.first { it.name == selectedEnvironment }.uuid
    }

    open fun getContentPanel(): JPanel? {
        return myContentPanel
    }
}