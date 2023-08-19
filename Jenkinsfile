pipeline {
    agent any
    tools {
        maven 'Maven_3.9.4'
    }
    stages {
        stage ('Build Maven') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage ('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t waleedehsan04/demo .'
                }
            }
        }
    }
}