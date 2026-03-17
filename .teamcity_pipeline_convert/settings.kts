import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.pipelines.*
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

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

version = "2025.11"

project {

    vcsRoot(HttpsGithubComDariaKrupAntProjectRefsHeadsMaster)

    pipeline(BookingApiPayconiqMavenPipeline)
}

object HttpsGithubComDariaKrupAntProjectRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/DariaKrup/AntProject#refs/heads/master"
    url = "https://github.com/DariaKrup/AntProject"
    branch = "refs/heads/master"
    branchSpec = "refs/heads/*"
    authMethod = password {
        userName = "DariaKrup"
        password = "credentialsJSON:a9f10039-e193-4665-afab-91b5b38ce930"
    }
    param("pipelines.connectionId", "PROJECT_EXT_238")
})


object BookingApiPayconiqMavenPipeline : Pipeline({
    name = "BookingApiPayconiq: Maven Pipeline"

    repositories {
        repository(DslContext.settingsRoot)
    }

    triggers {
        vcs {
            branchFilter = """
                +:*
                +pr:*
            """.trimIndent()
        }
    }

    job(BookingApiPayconiqMavenPipeline_Job1)
})

object BookingApiPayconiqMavenPipeline_Job1 : Job({
    id("Job1")
    name = "Maven tests"
    allowReuse = false
    enableDependencyCacheOptimization = false
    parallelism = 2

    steps {
        maven {
            name = "clean test"
            goals = "clean test"
        }
    }
})
