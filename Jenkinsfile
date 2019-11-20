pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        DEPLOY_BRANCH = 'develop'
    }

    stages {
        stage('Current environment variables') {
            steps {
                sh "printenv"
            }
        }
        stage('Set Version') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${build.number} versions:commit -Dbuild.number=${BUILD_NUMBER}'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B clean package -Dbuild.number=${BUILD_NUMBER}'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Tag Version') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'admin', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    echo "git push http://${GIT_USERNAME}:${GIT_PASSWORD}@bitbucket:7990/scm/tkd/simple.git  tag_${BUILD_NUMBER}"
                    sh ('git status')
                    sh("git tag -a tag_${BUILD_NUMBER} -m 'Tagging ${BUILD_NUMBER}'")
                    sh("git push http://${GIT_USERNAME}:${GIT_PASSWORD}@bitbucket:7990/scm/tkd/simple.git  tag_${BUILD_NUMBER}")
                }
            }
        }
        stage('Deploy') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                configFileProvider([configFile(fileId: 'global-settings-xml', variable: 'MAVEN_SETTINGS_XML')]) {
                    sh 'mvn deploy -s $MAVEN_SETTINGS_XML'
                }
            }
        }
    }
}