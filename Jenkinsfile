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
                sh '''
                    export CACHEBUST=$(git rev-parse HEAD)
                    docker compose build
                '''
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker tag order-management-system-order-service:latest $DOCKER_USER/order-service:$GIT_COMMIT
                        docker tag order-management-system-payment-service:latest $DOCKER_USER/payment-service:$GIT_COMMIT
                        docker tag order-management-system-notification-service:latest $DOCKER_USER/notification-service:$GIT_COMMIT
                        docker push $DOCKER_USER/order-service:$GIT_COMMIT
                        docker push $DOCKER_USER/payment-service:$GIT_COMMIT
                        docker push $DOCKER_USER/notification-service:$GIT_COMMIT
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                    sh '''
                        chmod 600 $SSH_KEY
                        ssh -o StrictHostKeyChecking=no -i $SSH_KEY ubuntu@3.231.206.191 "cd order-service && export IMAGE_TAG=$GIT_COMMIT && docker compose pull && docker compose up -d"
                        ssh -o StrictHostKeyChecking=no -i $SSH_KEY ubuntu@35.173.1.140 "cd payment-service && export IMAGE_TAG=$GIT_COMMIT && docker compose pull && docker compose up -d"
                        ssh -o StrictHostKeyChecking=no -i $SSH_KEY ubuntu@3.235.249.97 "cd notification-service && export IMAGE_TAG=$GIT_COMMIT && docker compose pull && docker compose up -d"
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