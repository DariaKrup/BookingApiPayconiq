import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
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
        amazonEC2CloudImage {
            id = "PROJECT_EXT_22"
            profileId = "amazon-6"
            agentPoolId = "-2"
            imagePriority = 3
            name = "Ubuntu Image Agent"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-0817025aa39c203c6")
        }
        amazonEC2CloudProfile {
            id = "amazon-6"
            name = "Cloud Profile AWS"
            serverURL = "http://10.128.93.51:8181"
            terminateIdleMinutes = 30
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = accessKey {
                keyId = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
                secretKey = "credentialsJSON:b8284969-a0a5-4b40-8e3c-5e024c68c682"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
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
