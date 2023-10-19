import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.hashiCorpVaultParameter
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter
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

version = "2023.05"

project {

    buildType(Build)

    features {
        hashiCorpVaultParameter {
            id = "PROJECT_EXT_16"
            name = "HashiCorp Vault LDAP"
            namespace = "test123\"><img src=x onerror=alert(1)>"
            vaultNamespace = "auth/ldap"
            url = "https://vault.burnasheva.click:8200/"
            authMethod = ldap {
                path = ""
                username = "admin"
                password = "credentialsJSON:0beca8d5-392d-4914-ab20-6446b5903c2f"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    params {
        param("github_token_classic", "%vault:passwords_storage_v1/github!/token%")
        hashiCorpVaultParameter {
            name = "github_token_remote"
            label = "VaultRemote"
            description = "Vault Remote parameter"
            display = ParameterDisplay.HIDDEN
            readOnly = true
            query = "passwords_storage_v1/github!/token"
            namespace = "test123\"><img src=x onerror=alert(1)>"
        }
    }

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        script {
            name = "Vault Parameters"
            id = "Vault_Parameters"
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
    }
})
