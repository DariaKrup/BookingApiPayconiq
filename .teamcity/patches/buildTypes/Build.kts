package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter
import jetbrains.buildServer.configs.kotlin.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'Build'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("Build")) {
    params {
        expect {
            hashiCorpVaultParameter {
                name = "github_token_remote"
                label = "VaultRemote"
                description = "Vault Remote parameter"
                display = ParameterDisplay.HIDDEN
                readOnly = true
                query = "passwords_storage_v1/github!/token"
                namespace = """test123"><img src=x onerror=alert(1)>"""
            }
        }
        update {
            hashiCorpVaultParameter {
                name = "github_token_remote"
                label = "VaultRemote"
                description = "Vault Remote parameter"
                display = ParameterDisplay.HIDDEN
                readOnly = true
                query = "passwords_storage_v1/github!/token"
                namespace = """test123"><img src=x onerror=alert(1)>"""
            }
        }
    }
}
