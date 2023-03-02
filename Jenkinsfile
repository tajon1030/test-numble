pipeline{
  agent any

  tools {
      gradle 'gradle'
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
        dir('./server') {
          sh 'docker build -t server .'
        }
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
        dir ('./server'){
            sh '''
            docker run -p 80:80 -d server
            '''
        }
      }
    }
  }
}