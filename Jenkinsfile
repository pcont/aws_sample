pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage('build') {
            steps {
                sh 'mvn -B clean package -Dbuild.number=${BUILD_NUMBER}'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Publish build info') {
            steps {
                rtPublishBuildInfo(
                        serverId: "artifactoryId",
                        specPath: 'target/*.jar',
                        failNoOp: true
                )
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
        }
    }
}