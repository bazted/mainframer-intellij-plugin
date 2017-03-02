package com.elpassion.intelijidea

import com.elpassion.intelijidea.task.MFBeforeRunTask
import com.elpassion.intelijidea.task.MFBeforeRunTaskProvider
import com.intellij.execution.BeforeRunTask
import com.intellij.execution.BeforeRunTaskProvider
import com.intellij.execution.RunManager
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.openapi.project.Project

class TaskManipulator(val project: Project, val mfTaskProvider: MFBeforeRunTaskProvider) {
    val runManager: RunManagerEx = RunManagerEx.getInstanceEx(project)

    fun injectMFToConfigurations(configurations: List<RunConfiguration>) {
        configurations.forEach { configuration ->
            runManager.getFirstMFBeforeRunTask(configuration)?.let {
                injectMFTask(configuration)
            }
        }
    }

    fun injectMFToConfigurationsWithReplacingMFTask(configurations: List<RunConfiguration>) {
        configurations.forEach {
            injectMFTask(it)
        }
    }

    private fun injectMFTask(configuration: RunConfiguration) {
        val taskToInject = mfTaskProvider.createEnabledTask(configuration)
        runManager.setBeforeRunTasks(configuration, listOf(taskToInject), false)
    }

    private fun RunManagerEx.getFirstMFBeforeRunTask(configuration: RunConfiguration) =
            getBeforeRunTasks(configuration).filterIsInstance<MFBeforeRunTask>().firstOrNull()

    private fun MFBeforeRunTaskProvider.createEnabledTask(runConfiguration: RunConfiguration) = createTask(runConfiguration)
            .apply { isEnabled = true }

    fun restoreConfigurations(configurations: List<RunConfiguration>) {
        configurations.forEach {
            restoreDefaultBeforeRunTasks(it)
        }
    }

    private fun restoreDefaultBeforeRunTasks(configuration: RunConfiguration) {
        val beforeRunTasks = getHardcodedBeforeRunTasks(configuration, project)
        runManager.setBeforeRunTasks(configuration, beforeRunTasks, false)
    }

}

@Deprecated("Remove when configuration dialog completed", ReplaceWith("Configuration list selected from dialog"))
fun RunManagerEx.getConfigurationsItems() =
        (getAllConfigurationsAsSelectorItems() + getTemplateConfigurationsAsSelectorItems())

fun RunManager.getTemplateConfigurations(): List<RunConfiguration> {
    val configurationTypes = TemplateConfigurationsProvider.get()
    return configurationTypes.flatMap { it.configurationFactories.toList() }
            .map { getConfigurationTemplate(it) }
            .map { it.configuration }
}

private fun RunManagerEx.getAllConfigurationsAsSelectorItems() = allConfigurationsList.filterInjectableConfigurations()

private fun RunManagerEx.getTemplateConfigurationsAsSelectorItems() = getTemplateConfigurations().filterInjectableConfigurations()

private fun getHardcodedBeforeRunTasks(configuration: RunConfiguration, project: Project): List<BeforeRunTask<*>> {
    val beforeRunProviders = BeforeRunTaskProvider.EXTENSION_POINT_NAME.getExtensions(project)
    return beforeRunProviders.associate { provider -> provider.id to provider.createTask(configuration) }
            .filterValues { task -> task != null && task.isEnabled }
            .map {
                configuration.factory.configureBeforeRunTaskDefaults(it.key, it.value)
                it.value
            }
            .filterNotNull()
            .filter { it.isEnabled }
}

private fun List<RunConfiguration>.filterInjectableConfigurations() = filterIsInstance<RunConfigurationBase>()
        .filter { it.isCompileBeforeLaunchAddedByDefault }