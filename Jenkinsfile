@Library('pipeline-shared@master') _

pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2 --network=data_default'
        }
    }

    environment {
        DEPLOY_BRANCH = 'develop'
        GIT_CREDENTIAL_ID = 'admin'
        POM = readMavenPom()
        PROJECT_VERSION = "${POM.version}"
    }

    stages {
        stage('Current environment variables') {
            steps {
                sh "printenv"
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
            when {
                branch "${DEPLOY_BRANCH}"
            }
            environment {
                TAG_VALUE = "V_${PROJECT_VERSION}.${BUILD_NUMBER}"
                GIT_URL_WITH_AUTH = authUrl "${GIT_URL}", "${GIT_CREDENTIAL_ID}"
            }
            steps {
                sh("git checkout ${GIT_BRANCH}")
                sh("git tag ${TAG_VALUE}")
                sh("git push ${GIT_URL_WITH_AUTH} ${TAG_VALUE}")
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