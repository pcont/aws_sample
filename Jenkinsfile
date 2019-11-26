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
        DIR_VERSION_REPO = 'artifactVersions'
        FILE_VERSION_REPO = 'version-code.yml'
        BRANCH_VERSION_REPO = 'master'
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
            steps {
                tagGit "V_${PROJECT_VERSION}", "${GIT_BRANCH}"
            }
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
                cloneVersionRepo("${DIR_VERSION_REPO}", "${GIT_VERSION_REPO}", "${BRANCH_VERSION_REPO}")

            }
        }
        stage('Read yam version file') {
            steps {
                updateVersionRepo("${DIR_VERSION_REPO}/${FILE_VERSION_REPO}", "${ARTIFACT_ID}", "${PROJECT_VERSION}")
            }
        }
        stage('Push Artifact Version to Repository') {
            environment {
                GIT_AUTH = credentials("${GIT_CREDENTIAL_ID}")
                ARTIFACT_NAME = "${readMavenPom()}"
            }
            steps {
                pushVersionRepo("${DIR_VERSION_REPO}", "${FILE_VERSION_REPO}", "${GIT_VERSION_REPO}")


//                dir('artifactVersions') {
//                    sh('''
//git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"
//git add "version-code.yml"
//git commit -m "Updating version ${ARTIFACT_NAME}"
//git push "${GIT_VERSION_REPO}"
//''')
//                }
            }
        }
    }
}