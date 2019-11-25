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
        PROJECT_VERSION = projectVersion()
        ARTIFACT_ID = "${readMavenPom().artifactId}"
        GIT_VERSION_REPO = 'http://bitbucket:7990/scm/tkd/deploy-local.git'
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
            steps{
                tagGit()
            }
//            environment {
//                TAG_VALUE = "V_${PROJECT_VERSION}"
//                GIT_URL_WITH_AUTH = authUrl "${GIT_URL}", "${GIT_CREDENTIAL_ID}"
//            }
//            steps {
//                sh("git checkout ${GIT_BRANCH}")
//                sh("git tag ${TAG_VALUE}")
//                sh("git push ${GIT_URL_WITH_AUTH} ${TAG_VALUE}")
//            }
        }
        stage('Deploy') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
//            todo deploy without rebuilding (if possible)
//            beware of the jar name
            steps {
                configFileProvider([configFile(fileId: 'global-settings-xml', variable: 'MAVEN_SETTINGS_XML')]) {
                    sh 'mvn deploy -s $MAVEN_SETTINGS_XML -Dmaven.install.skip'
                }
            }
        }
        stage('Clone Artifact Version Repository') {
            steps {
                sh 'rm artifactVersions -rf; mkdir artifactVersions'
                dir('artifactVersions') {
                    git branch: 'master',
                            credentialsId: "${GIT_CREDENTIAL_ID}",
                            url: "${GIT_VERSION_REPO}"
                }
            }
        }
        stage('Read yam version file') {
            steps {
                updateVersion('artifactVersions/version-code.yml', "${ARTIFACT_ID}", "${PROJECT_VERSION}")
            }
        }
        stage('Push Artifact Version to Repository') {
            environment {
                GIT_AUTH = credentials( "${GIT_CREDENTIAL_ID}")
                ARTIFACT_NAME = "${readMavenPom()}"
            }
            steps {
                dir('artifactVersions') {
                    sh('''
git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
git add "version-code.yml"
git commit -m "Updating version ${ARTIFACT_NAME}"
git push "${GIT_VERSION_REPO}"
''')
                }
            }
        }
    }
}