#!groovy

DEFAULT_LINK_URL = env.BUILD_URL
POSTMAN_COLLECTION_UID = "22637373-a4ce3762-f507-4ba2-b0e6-e5210924e192"
POSTMAN_ENVIRONMENT_UID = "22637373-ab571385-77e9-4cd2-b047-6b53861f4348"
POSTMAN_API_KEY = "PMAK-62f34decfeeb3b6a68eea9e7-6afc180573e225485a8a3e71971ff8508d"

properties([
    disableConcurrentBuilds()
])

node {
   
    env.JAVA_HOME = "${jdk}"

    stage('Checkout') {
        checkout([
                $class                         : 'GitSCM',
                branches                         : scm.branches,
                doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                extensions                     : [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
                userRemoteConfigs               : scm.userRemoteConfigs,
        ])
    }

    stage('IntegrationTest') {
        integrationTest()
    }
}

def integrationTest() {
    try {
        sh "newman run ~/Downloads/ilgoo-test-collection.json"
    } catch(e) {
        throw e
    }
}

def isPR() {
    return (env.BRANCH_NAME ==~ /^PR-\d+$/)
}


@NonCPS
def jsonParse(def json) {
    new groovy.json.JsonSlurperClassic().parseText(json)
}

