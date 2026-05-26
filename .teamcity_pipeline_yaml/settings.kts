import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.pipelines.*
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

version = "2026.1"

project {

    buildType(BuildNumberReport)

    pipeline(MavenPipeline)
}

object BuildNumberReport : BuildType({
    name = "Build number report"

    steps {
        script {
            id = "simpleRunner"
            scriptContent = "echo %build.counter%"
        }
    }
})


object MavenPipeline : Pipeline({
    name = "Maven Pipeline"

    repositories {
        repository(AbsoluteId("TeamCity_Sandbox_DKrupkinaSandbox_MavenUnbalancedMessages"))
    }

    triggers {
        vcs {
        }
    }

    job(MavenPipeline_Job1)
})

object MavenPipeline_Job1 : Job({
    id("Job1")
    name = "Job 1"

    steps {
        maven {
            name = "clean test"
            goals = "clean test"
        }
    }
})
