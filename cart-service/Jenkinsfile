//repo-per-microservice

node{
    stage('SCM Checkout from GitHub'){
        git 'https://github.com/sushantac/scshop-cart-service'
    }
    
    stage('MVN Package'){
        def mvnHome = tool name: 'Apache Maven', type: 'maven'
        def mvnCMD = "${mvnHome}/bin/mvn"
        
        sh label: '', script: "\"${mvnCMD}\" clean package -f pom.xml"
    }
    
    stage('Build Docker Image') {
        sh label: '', script: 'docker build -t sushantac/cart-service:0.0.1 --file Dockerfile .'
    }
    
    stage('Push to docker hub') {
	
	    withCredentials([string(credentialsId: 'docker-pwd', variable: 'dockerHubPassword')]) {
            sh label: '', script: "docker login -u sushantac -p ${dockerHubPassword}"
        }

        sh label: '', script: 'docker push sushantac/cart-service:0.0.1'
    }

    
    
}