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
      } // ansiColor
  } // stage 2
} // try end
catch (e) {
    mail to: 'gratien.dhaese@gmail.com',
      from: 'noreply@jenkins',
      subject: "Jenkins Failed: ${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
      body: "Project build (${BUILD_TAG}) failed '${e}'"

   FAILURE = e

} finally {
      stage('Cleanup') {
      sh """
        set +e;

        exit 0;
      """

      if (FAILURE) {
        currentBuild.result = 'FAILURE'
        throw FAILURE
      } // if
    } //cleanup

 
 // Must re-throw exception to propagate error:
 if (e) {
     throw e
 } // err
} // finally
