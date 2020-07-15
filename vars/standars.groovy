def call (String myName='Lilouch')
{
pipeline {
    agent any
      environment {
        NAME = '$myName'
        LASTNAME = 'Muller'
        secret = credentials('My_SECRET')

    }
    stages {


stage('Test_lib') {
            steps {
                
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE'){
                timeout(time: 6, unit: 'SECONDS') {
                   retry(3) {
                    sh 'echo hello'
                     sh 'sleep 3'
                }
                }
                sh 'echo 2nd step of the Test stage: execute by $NAME , $LASTNAME'
                
                }
            }
            
        }

stage('CodeFixAndValidation_lib') {
 parallel {
      // One or more stages need to be included within the parallel block.

       stage(CodeFix)
{


            steps {
	sh 'echo "success !"; exit 0'
            }

}


stage(CodeValidation){

steps{

sh 'echo "the Code Validation Stage"; exit 0'

}




}

    }//parllel
    }



stage('Final_lib')
        {
            steps
            {
                sh """
                   echo 'this is the final stage'
                   echo 'by the way this is the secret of $NAME : $secret ' 
                     
                   """ 
                
                
            }
            
            
            
        }

}       
post {
        always {
            echo 'I will always get executed :D'
        }
        success {
            echo 'I will only get executed if this success'
        }
        failure {
            echo 'I will only get executed if this fails'
        }
        unstable {
            echo 'I will only get executed if this is unstable'
        }
    }
}





























}
