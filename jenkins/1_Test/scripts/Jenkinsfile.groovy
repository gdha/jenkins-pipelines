#!/usr/bin/env groovy

// see https://wilsonmar.github.io/jenkins2-pipeline/ for an example

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
node {
stage '\u2776 Stage 1'
echo "\u2600 BUILD_URL=${env.BUILD_URL}"
 
def workspace = pwd()
echo "\u2600 workspace=${workspace}"
 
stage '\u2777 Stage 2'
} // node
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
  
 (currentBuild.result != "ABORTED") && node("master") {
     // Send e-mail notifications for failed or unstable builds.
     // currentBuild.result must be non-null for this step to work.
     step([$class: 'Mailer',
        notifyEveryUnstableBuild: true,
        recipients: "${email_to}",
        sendToIndividuals: true])
 }
 
 // Must re-throw exception to propagate error:
 if (err) {
     throw err
 }
}
