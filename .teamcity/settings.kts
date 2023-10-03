import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudImage
import jetbrains.buildServer.configs.kotlin.amazonEC2CloudProfile
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
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

    params {
        password("reverse.dep.*.password.param", "credentialsJSON:21597784-9a41-4aab-a8ee-f2b5315fb112")
    }

    features {
        amazonEC2CloudImage {
            id = "PROJECT_EXT_5"
            profileId = "amazon-2"
            agentPoolId = "-2"
            name = "Image Ubuntu"
            vpcSubnetId = "subnet-0c23f411b0800b216"
            keyPairName = "daria.krupkina"
            instanceType = "t2.medium"
            securityGroups = listOf("sg-072d8bfa0626ea2a6")
            source = Source("ami-07908fe7a17542f6b")
        }
        amazonEC2CloudProfile {
            id = "amazon-2"
            name = "Cloud Profile AWS"
            serverURL = "http://10.128.93.51:8181"
            terminateIdleMinutes = 0
            region = AmazonEC2CloudProfile.Regions.EU_WEST_DUBLIN
            authType = accessKey {
                keyId = "credentialsJSON:5956c87f-9f8f-4ec4-8c89-2874bed09e35"
                secretKey = "credentialsJSON:42f04976-3912-4b71-8161-3e9ca9484e7d"
            }
        }
    }
}

object Build : BuildType({
    name = "Build"

    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("parameter_for_dsl", "default")
        password("password.param", "credentialsJSON:c471b542-ee2a-49d0-8361-fb34ffec62c2")
        param("env.JDK_17_0_x64", "%env.JRE_HOME%")
    }

    vcs {
        root(DslContext.settingsRoot)

        showDependenciesChanges = true
    }

    steps {
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=false"
            jdkHome = "%env.JDK_17_0_x64%"
        }
        script {
            name = "Output of password.param"
            enabled = false
            scriptContent = "echo %password.param% > param_output_.out"
        }
    }

    triggers {
        vcs {
        }
    }

    failureConditions {
        executionTimeoutMin = 2
        testFailure = false
        nonZeroExitCode = false
    }

    features {
        perfmon {
        }
    }

    dependencies {
        snapshot(AbsoluteId("JavaMavenDemo_Build")) {
        }
    }
})
