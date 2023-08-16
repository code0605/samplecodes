import hudson.model.*

def cutoffDate = System.currentTimeMillis() - (90 * 24 * 60 * 60 * 1000) // 90 days in milliseconds

Jenkins.instance.getAllItems(Job).each { job ->
    def lastBuild = job.getLastBuild()
    if (lastBuild) {
        def lastBuildTimestamp = lastBuild.getTimeInMillis()
        if (lastBuildTimestamp < cutoffDate) {
            println("Job '${job.fullName}' has not been executed in the last 90 days.")
        }
    } else {
        // Job has never been built
        println("Job '${job.fullName}' has not been executed yet.")
    }
}
