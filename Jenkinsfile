@Library('pipeline-shared@master') _

pipeline {
    agent {
        docker {
            image 'maven:3.6.0'
            args '-v /root/.m2:/root/.m2 --network=tkd_tkdnetwork'
        }
    }

    environment {
        DEPLOY_BRANCH = 'develop'
        GIT_CREDENTIAL_ID = 'admin'
        ARTIFACTORY_URL = 'http://artifactory:8081/artifactory/libs-release-local/'
        GIT_VERSION_REPO = 'http://bitbucket:7990/scm/tkd/deploy-local.git'
        FILE_VERSION_REPO = 'version-code.yml'
        BRANCH_VERSION_REPO = 'master'



        PROJECT_VERSION = projectVersion()
        ARTIFACT_ID = "${readMavenPom().artifactId}"
        DIR_VERSION_REPO = 'artifactVersions'
    }

    stages {
        stage('Environment Variables') {
            steps {
                sh "printenv"
            }
        }
        stage('Set Pom Version') {
            steps {
                sh "mvn versions:set versions:commit -DnewVersion=${PROJECT_VERSION}"
            }
        }
        stage('Build & Test') {
            steps {
                sh "mvn -B clean verify"
            }
            post {
                always {
                    junit "target/surefire-reports/*.xml"
                }
            }
        }
        stage('Sonarqube'){
            environment{
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "mvn sonar:sonar"
                }
            }
        }
        stage('Tag Version') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                tagGit "${PROJECT_VERSION}", "${GIT_BRANCH}"
            }
        }
        stage('Deploy') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                deploy("${ARTIFACTORY_URL}")
            }
        }
        stage('Clone Artifact Version Repository') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                cloneVersionRepo "${DIR_VERSION_REPO}", "${GIT_VERSION_REPO}", "${BRANCH_VERSION_REPO}"
            }
        }
        stage('Read Yaml Version File') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                updateVersionRepo "${DIR_VERSION_REPO}/${FILE_VERSION_REPO}", "${ARTIFACT_ID}", "${PROJECT_VERSION}"
            }
        }
        stage('Push Artifact Version to Repository') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                pushVersionRepo "${DIR_VERSION_REPO}", "${FILE_VERSION_REPO}", "${GIT_VERSION_REPO}"
            }
        }
    }
}
