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
    
    withCheckout(scm) {
        echo "git commit is ${env.GIT_COMMIT}"
        sh "if git rev-parse --verify -q ${env.GIT_COMMIT}^2 > /dev/null; then " +
                               "echo 'commit ${env.GIT_COMMIT} is a merge commit' " +
                            "else " +
                               "echo 'commit ${env.GIT_COMMIT} is a simple commit' " + 
                        "fi"
    }
    
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

