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
    
    
    stage('IntegrationTest') {
        integrationTest()
    }
}

def integrationTest() {
    nodeJsHome = tool name: 'nodejs', type: 'nodejs'
    newmanHome = "${nodeJsHome}/bin"
    
    commitName = checkout(scm).GIT_COMMIT
        echo "git commit is ${commitName}"
        sh "#!/bin/sh " +
           "if git rev-parse --verify -q ${commitName}^2 > /dev/null; " + 
           "then echo `1111` else echo '2222' fi"

    try {
        nodejs('nodejs') {
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

