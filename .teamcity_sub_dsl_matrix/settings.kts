import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.matrix
import jetbrains.buildServer.configs.kotlin.projectFeatures.hashiCorpVaultConnection
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.07"

project {

    features {
        hashiCorpVaultConnection {
            id = "PROJECT_EXT_16"
            name = "HashiCorp Vault LDAP"
            vaultId = "test123"
            vaultNamespace = "auth/ldap"
            url = "https://vault.burnasheva.click:8200/"
            authMethod = ldap {
                path = "ldap/ldap"
                username = "admin"
                password = "credentialsJSON:0beca8d5-392d-4914-ab20-6446b5903c2f"
            }
        }
    }

    subProject(SubProject)
}


object SubProject : Project({
    name = "SubProject"

    buildType(Build)
})

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Vault Parameters"
            id = "Vault_Parameters"
            enabled = false
            scriptContent = "echo %github_token_classic% %github_token_remote% >> tokens.txt"
        }
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
        matrix {
            os = listOf(
                value("Linux"),
                value("Mac OS")
            )
        }
    }
})
