pipeline{
  agent any

  tools {
      gradle 'gradle7.6'
  }

  stages{
    stage('Prepare'){
      steps{
        echo 'Clonning Repository'
        git branch: 'main',
          credentialsId: 'tajon1030',
          url: 'https://github.com/tajon1030/timedeal-numble'
      }
      post {
        success {
          echo 'Successfully Cloned Repository'
        }
        always {
          echo "i tried..."
        }
        cleanup {
          echo "after all other post condition"
        }
      }
    }

    stage('Build'){
      steps{
        echo 'Build'
        sh 'chmod 755 ./gradlew'
        sh './gradlew clean build'
        sh 'docker build -t server .'
      }

      post {
        failure {
          error 'This pipeline stops here...'
        }
      }
    }

    stage('Deploy') {
      agent any

      steps {
        sh 'docker run -p 80:80 -d server'
      }
    }
  }
}