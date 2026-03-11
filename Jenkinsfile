def gv

pipeline {
    agent any
    tools {
        nodejs 'nodejs-25'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load 'script.groovy'
                }
            }
        }
        stage("increment version") {
            steps {
                script {
                    gv.incrementVersion()
                }
            }
        }
        stage("test") {
            steps {
                script {
                    gv.testApp()
                }
            }
        }
        stage("build docker image") {
            steps {
                script {
                    gv.buildDockerImage()
                }
            }
        }
        stage("push docker image to repository") {
            steps {
                script {
                    gv.pushDockerImage()
                }
            }
        }
        stage("commit version change to github repository") {
            steps {
                script {
                    gv.gitPush()
                }
            }
        }
    }
}