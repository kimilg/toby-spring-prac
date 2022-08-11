#!groovy

DEFAULT_LINK_URL = env.BUILD_URL

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

