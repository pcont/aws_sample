pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        DEPLOY_BRANCH = 'develop'
        GIT_CREDENTIAL_ID = 'admin'
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
//            when {
//                branch "${DEPLOY_BRANCH}"
//            }
            steps {
                environment{
                    PROJECT_VERSION = sh ("mvn help:evaluate -Dexpression=project.version -q -DforceStdout")
                    TAG_VALUE = "${PROJECT_VERSION}.${BUILD_NUMBER}"
                }

                withCredentials([usernamePassword(credentialsId: "${GIT_CREDENTIAL_ID}", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {

//                    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\${parsedVersion.majorVersion}.\\${parsedVersion.minorVersion}.\\${build.number} versions:commit -Dbuild.number=${BUILD_NUMBER}'
                    sh("git checkout ${GIT_BRANCH}")
                    sh("git tag ${TAG_VALUE}")
                    sh("git push http://${GIT_USERNAME}:${GIT_PASSWORD}@172.17.0.1:7990/scm/tkd/simple.git ${TAG_VALUE}")
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