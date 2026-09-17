pipeline {
    agent any

    tools {
        gradle 'Gradle' 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java') {
            steps {
                sh 'java -version'
                sh 'which java'
            }
        }

        stage('Build Order Service') {
            steps {
                dir('order-service') {
                    sh './gradlew clean build'
                }
            }
        }

        stage('Build Payment Service') {
            steps {
                dir('payment-service') {
                    sh './gradlew clean build'
                }
            }
        }

        stage('Build Notification Service') {
            steps {
                dir('notification-service') {
                    sh './gradlew clean build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build'
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed — check logs above.'
        }
    }
}