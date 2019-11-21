@Library('jenkins-shared@master') _

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
    }

    stages {
        stage('Current environment variables') {
            steps {
                sh "printenv"
            }
        }
        stage('Set Version') {
//            todo remove this stage
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
            environment {
//                todo investigate snapshot case
                PROJECT_VERSION = """${
                    sh(
                            returnStdout: true,
                            script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout"
                    )
                }"""
                TAG_VALUE = "${PROJECT_VERSION}.${BUILD_NUMBER}"
                GIT_ACCESS = credentials("${GIT_CREDENTIAL_ID}")
                GIT_URL_WITH_AUTH =   authUrl(
                        url: "${GIT_URL}",
                        usr: "${GIT_ACCESS_USR}",
                        psw: "${GIT_ACCESS_PSW}"
                )

//                GIT_URL_WITH_AUTH = """${
//                    sh(
//                            returnStdout: true,
//                            script: '"my://test".replace("://", "://response")'
//                    )
//                }"""


//                GIT_URL_WITH_AUTH = """${
//                    sh(
//                            returnStdout: true,
//                            script: "my://test.replace('://', '://response')"
////                            script: '"${GIT_URL}".replace("://", "://${GIT_ACCESS_USR}:${GIT_ACCESS_PSW}@")'
//                    )
//                }"""
            }
            steps {

                sh "printenv"
//                withCredentials([usernamePassword(credentialsId: "${GIT_CREDENTIAL_ID}", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                sh("git checkout ${GIT_BRANCH}")
                sh("git tag ${TAG_VALUE}")
                sh("git push ${GIT_URL_WITH_AUTH} ${TAG_VALUE}")
//                }
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