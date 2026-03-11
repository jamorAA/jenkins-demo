def incrementVersion() {
    dir ('app') {
        sh "npm version patch --no-git-tag-version"
        def version = sh(script: "npm pkg get version", returnStdout: true).trim()
        env.DOCKER_IMAGE_VERSION = "${version}-${BUILD_NUMBER}"
    }
}

def testApp() {
    dir ('app') {
        sh "npm install"
        sh "npm run test"
    }
}

def buildDockerImage() {
    sh "docker build -t jamoraa/nodejs-app:${DOCKER_IMAGE_VERSION} ."
}

def pushDockerImage() {
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push jamoraa/nodejs-app:${DOCKER_IMAGE_VERSION}"
    }
}

def gitPush() {
    withCredentials([string(credentialsId: 'github-token', variable: 'TOKEN')]) {
        sh "git status"
        sh "git branch"
        sh "git config --list"

        sh "git remote set-url origin https://${TOKEN}@github.com/jamorAA/jenkins-demo.git"

        sh "git add app/package.json"
        sh "git commit -m 'ci pipeline: increment version'"
        sh "git push origin HEAD:main"
    }
}
return this
