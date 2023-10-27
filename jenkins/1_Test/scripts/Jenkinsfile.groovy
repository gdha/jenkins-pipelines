import org.jenkinsci.plugins.pipeline.modeldefinition.Utils
import groovy.text.SimpleTemplateEngine
import org.apache.commons.io.FileUtils
import hudson.model.*
import jenkins.model.Jenkins
import groovy.json.*

// Inputs
def yyyyMM = java.time.LocalDate.now().plusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern('yyyyMM'))

// vars
def shEx = "set -ex"

// Paths
def workspace = sh returnStdout: true, script: 'realpath ${WORKSPACE}/workspace'
def yyyyMMWS = "${workspace}/${yyyyMM}"

ws("${yyyyMMWS}/ws") {
  withEnv(["shEx=${shEx}", "PATH=/usr/local/bin:/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/usr/bin/python:/usr/local/bin/aws"]) {
    try {
      /* Stage: Init
       * Get list of all supported bundles
       * Get list of default bundles
       */

stage('Init') {
        // build name/description
            sh script: """
              $shEx
              # Clear state
              cd /app/yumreposerver; python3 lib/certState.py --clear --jira $jira
            """
      } // Stage:Init
   } // try
  } // withEnv
} // ws("${yyyyMMWS}/ws")
