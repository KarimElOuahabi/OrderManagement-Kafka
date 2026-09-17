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
        
        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker tag order-management-system-order-service:latest $DOCKER_USER/order-service:latest
                        docker tag order-management-system-payment-service:latest $DOCKER_USER/payment-service:latest
                        docker tag order-management-system-notification-service:latest $DOCKER_USER/notification-service:latest
                        docker push $DOCKER_USER/order-service:latest
                        docker push $DOCKER_USER/payment-service:latest
                        docker push $DOCKER_USER/notification-service:latest
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ubuntu@13.218.111.189 "cd order-service && docker compose pull && docker compose up -d"
                        ssh -o StrictHostKeyChecking=no ubuntu@100.56.102.52 "cd payment-service && docker compose pull && docker compose up -d"
                        ssh -o StrictHostKeyChecking=no ubuntu@32.196.161.211 "cd notification-service && docker compose pull && docker compose up -d"
                    '''
                }
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