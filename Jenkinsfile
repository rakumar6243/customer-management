pipeline {
    agent any
    tools {
        maven 'maven_3_5_0'
    }
    stages {
        stage('Build') {
            steps {
                script {
                    // Checkout the code
                    checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Java-Techie-jt/devops-automation']]])

                    // Run Maven build
                    sh 'mvn clean install'

                    // Run SonarQube analysis
                    withSonarQubeEnv('SonarQubeServer') { // 'SonarQubeServer' should match your Jenkins configuration
                        sh 'mvn sonar:sonar -Dsonar.projectKey=devops-automation -Dsonar.host.url=http://<SONARQUBE_SERVER_URL> -Dsonar.login=<SONARQUBE_TOKEN>'
                    }

                    // Verify SonarQube quality gate
                    def sonarQubeResult = waitForQualityGate() // Waits for SonarQube Quality Gate to pass
                    if (sonarQubeResult.status != 'OK') {
                        error "SonarQube Quality Gate failed: ${sonarQubeResult.status}"
                    }

                    // Build Docker image
                    sh 'docker build -t customermanagement .'

                    // Push Docker image to Docker Hub
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        sh 'docker login -u username -p ${dockerhubpwd}'
                        sh 'docker push customermanagement'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Deploy to Kubernetes
                    kubernetesDeploy(configs: 'deploymentservice.yaml', kubeconfigId: 'k8sconfigpwd')
                }
            }
        }
    }
}
