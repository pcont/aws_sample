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
        POM_GROUP_ID = "${readMavenPom().groupId}"
        POM_VERSION = "${readMavenPom().version}"
        ARTIFACT_ID = "${readMavenPom().artifactId}"
        POM_PACKAGING = "${readMavenPom().packaging}"

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
                sh 'mvn versions:set versions:commit -DnewVersion="${PROJECT_VERSION}"'
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
                tagGit "${PROJECT_VERSION}", "${GIT_BRANCH}"
            }
        }
        stage('Deploy') {
//            when {
//                branch "${DEPLOY_BRANCH}"
//            }
            steps {
                configFileProvider([configFile(fileId: 'global-settings-xml', variable: 'MAVEN_SETTINGS_XML')]) {
                    echo """mvn org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy-file -DgroupId=${POM_GROUP_ID}  \
-DartifactId=${ARTIFACT_ID} \
-Dversion=${PROJECT_VERSION} \
-Dpackaging=${POM_PACKAGING} \
-Dfile=target/${ARTIFACT_ID}-${PROJECT_VERSION}.jar \
-DpomFile=pom.xml \
-DrepositoryId=artifactoryId \
-Durl=http://artifactory:8081/artifactory/libs-release-local/
"""

                    sh """mvn org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy-file -DgroupId=${POM_GROUP_ID}  \
-DartifactId=${ARTIFACT_ID} \
-Dversion=${PROJECT_VERSION} \
-Dpackaging=${POM_PACKAGING} \
-Dfile=target/${ARTIFACT_ID}-${PROJECT_VERSION}.jar \
-DpomFile=pom.xml \
-DrepositoryId=artifactoryId \
-Durl=http://artifactory:8081/artifactory/libs-release-local/
"""
                }
            }
        }
        stage('Clone Artifact Version Repository') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                cloneVersionRepo("${DIR_VERSION_REPO}", "${GIT_VERSION_REPO}", "${BRANCH_VERSION_REPO}")
            }
        }
        stage('Read yam version file') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                updateVersionRepo("${DIR_VERSION_REPO}/${FILE_VERSION_REPO}", "${ARTIFACT_ID}", "${PROJECT_VERSION}")
            }
        }
        stage('Push Artifact Version to Repository') {
            when {
                branch "${DEPLOY_BRANCH}"
            }
            steps {
                pushVersionRepo("${DIR_VERSION_REPO}", "${FILE_VERSION_REPO}", "${GIT_VERSION_REPO}")
            }
        }
    }
}