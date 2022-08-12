#!groovy

DEFAULT_LINK_URL = env.BUILD_URL

properties([
    disableConcurrentBuilds()
])
node {

    stage('Checkout') {
        checkout([
                $class                         : 'GitSCM',
                branches                         : scm.branches,
                doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                extensions                     : [[$class: 'CloneOption', noTags: false, shallow: false, depth: 0, reference: '']],
                userRemoteConfigs               : scm.userRemoteConfigs,
        ])
    }
    BRANCH_NAME = env.BRANCH_NAME
    CHANGE_TARGET = env.CHANGE_TARGET
    CHANGE_BRANCH = env.CHANGE_BRANCH
    BRANCH_IS_PRIMARY = env.BRANCH_IS_PRIMARY 
    
    
    stage('IntegrationTest') {
        integrationTest()
    }
}

def integrationTest() {
    nodeJsHome = tool name: 'nodejs', type: 'nodejs'
    newmanHome = "${nodeJsHome}/bin"
    
    try {
        nodejs('nodejs') {
            echo "${BRANCH_NAME}"
            echo "${CHANGE_TARGET}"    
            echo "${CHANGE_BRANCH}" 
            echo "${BRANCH_IS_PRIMARY}"
            sh "node -v"
            sh "${newmanHome}/newman run ~/Downloads/ilgoo-test-collection.json"
        }
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

