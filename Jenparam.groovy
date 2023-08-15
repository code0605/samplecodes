def paramName = 'YOUR_PARAMETER_NAME'

Jenkins.instance.getAllItems(Job).each { job ->
    def params = job.getBuildWrappersList().get(hudson.model.ParametersDefinitionProperty)
    if (params) {
        params.getParameterDefinitions().each { paramDef ->
            if (paramDef.name == paramName) {
                echo "Job: ${job.fullName}"
            }
        }
    }
}
