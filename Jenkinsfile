#!groovy

DEFAULT_LINK_URL = env.BUILD_URL

properties([
    disableConcurrentBuilds()
])
properties([pipelineTriggers([githubPullRequests(events: [close()], spec: '', triggerMode: 'HEAVY_HOOKS'), githubPush()])])
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
    
    if(isMergeCommit()){
        echo "wow merge commit!"
    }
    else {
        echo "wow not merge commit@!"
    }
    
    stage('IntegrationTest') {
        integrationTest()
    }
}

def isMergeCommit() { 
    commitName = checkout(scm).GIT_COMMIT
    return sh (
        script : "git rev-parse --verify -q ${commitName}^2 > /dev/null;",  
        returnStatus: true
        ) == 0
}
 
def integrationTest() {
    nodeJsHome = tool name: 'nodejs', type: 'nodejs'
    newmanHome = "${nodeJsHome}/bin"    
    commitName = checkout(scm).GIT_COMMIT
    branchName = checkout(scm).BRANCH_NAME
    echo "git commit is ${commitName}"
    echo "branch name is " + scm.branches[0].name //test-jenkins
    echo "branch name 2 is " + checkout(scm).GIT_BRANCH //origin/test-jenkins
    
    echo "원래 branch is " + env.BRANCH_NAME
    echo "target branch is " + env.CHANGE_TARGET
    
    echo "job name : " + env.JOB_NAME 
    
    /*sh '''#!/bin/sh +x
       if `git rev-parse --verify -q ${commitName}^2 > /dev/null;`  
       then 
          echo "This commit is merge commit : ${commitName}" 
       else 
          echo "This commit is NOT merge commit : ${commitName}"
       fi'''*/
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

