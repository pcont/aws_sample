pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args "-v /root/.m2:/root/.m2"
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
                def uploadSpec ="""{
  "files": [
    {
      "pattern": "target/**.jar",
      "target": "libs-release-local"
    }
 ]
}
"""
                server.upload spec: uploadSpec

            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
        }
    }
}