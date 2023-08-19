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
        stage ('Push Image to Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DockerPwd', variable: 'DOCKER_PASSWORD')]){
                    sh 'docker login -u waleedehsan04 -p ${DOCKER_PASSWORD}'
                    }
                    sh 'docker push waleedehsan04/demo'
                }
            }
        }
    }
}