#!/usr/bin/env groovy

// see https://wilsonmar.github.io/jenkins2-pipeline/ for an example
// Another nice example: https://code.usgs.gov/ghsc/hazdev/earthquake-design-ws/-/blob/v0.8.0/Jenkinsfile.groovy

import org.jenkinsci.plugins.pipeline.modeldefinition.Utils
import org.apache.commons.io.FileUtils
import hudson.EnvVars
import hudson.model.*
import jenkins.model.Jenkins
import groovy.text.SimpleTemplateEngine
import groovy.json.*

// Inputs
def yyyyMM = java.time.LocalDate.now().plusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern('yyyyMM'))

// vars
def shEx = "set -ex"

// Paths

try {
  stage '\u2776 Stage 1' {
  echo "\u2600 BUILD_URL=${env.BUILD_URL}"
 
  def workspace = pwd()
  echo "\u2600 workspace=${workspace}"
 
      ansiColor('xterm') {
        sh """
          mkdir -p \
            ${WORKSPACE}/coverage \
            ${WORKSPACE}/owasp-data \
          ;

          chmod -R 777 \
            ${WORKSPACE}/coverage \
            ${WORKSPACE}/owasp-data \
          ;
        """
      } // ansiColor
    } // stage 1

  stage '\u2777 Stage 2' {
    echo "\u2600 BUILD_ID=${env.BUILD_ID}"

          ansiColor('xterm') {
        sh """
           ls -lR / ;
        """
  } // stage 2
} // try end
catch (exc) {
/*
 err = caughtError
 currentBuild.result = "FAILURE"
 String recipient = 'infra@lists.jenkins-ci.org'
 mail subject: "${env.JOB_NAME} (${env.BUILD_NUMBER}) failed",
         body: "It appears that ${env.BUILD_URL} is failing, somebody should do something about that",
           to: recipient,
      replyTo: recipient,
 from: 'noreply@ci.jenkins.io'
*/
} finally {
  
 (currentBuild.result != "ABORTED") {
     // Send e-mail notifications for failed or unstable builds.
     // currentBuild.result must be non-null for this step to work.
     step([$class: 'Mailer',
        notifyEveryUnstableBuild: true,
        recipients: "${email_to}",
        sendToIndividuals: true])
 } // currentBuild.result
 
 // Must re-throw exception to propagate error:
 if (err) {
     throw err
 } // err
} // finally
