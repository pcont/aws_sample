pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {
        stage('Artifactory configuration'){
            rtServer (
                id: 'ArtifactoryId',
                url: 'http://artifactory',
                // If you're using username and password:
                username: 'admin',
                password: 'password'
                // If Jenkins is configured to use an http proxy, you can bypass the proxy when using this Artifactory server:
                bypassProxy: true
             )
        }
        stage('build') {
            steps {
                sh 'mvn -B clean package -Dbuild.number=${BUILD_NUMBER}'
            }
            post{
                always{
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage ('Publish build info') {
           steps {
                rtPublishBuildInfo (
                    serverId: "ArtifactoryId",
                    specPath: 'target/*.jar',
                    failNoOp: true
                )
            }
        }
    }

    post{
        always{
            archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
        }
    }
}