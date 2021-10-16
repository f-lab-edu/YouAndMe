node {
   stage ('clone') {
       checkout scm
   }
   stage('build') {
       sh 'chmod +555 ./gradlew'
       sh './gradlew clean build'
   }
}
