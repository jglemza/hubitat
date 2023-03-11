/**
*   
*   File: mattermost.groovy
*   Requirements: Requires a Mattermost webhook url.
*   Platform: Hubitat
*   Modification History:
*     2023-03-11    Initial Release
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*/

preferences {
    input("mm_url", "text", title: "Webhook URL:", description: "Incoming webhook URL", required: true)
    input("mm_channel", "text", title: "Channel:", description: "Override the default channel", required: false)
    input("mm_username", "text", title: "Username:", description: "Override the default username", required: false)
    input("mm_icon_url", "text", title: "Icon URL:", description: "Override the default profile picture", required: false)
    input("mm_icon_emoji", "text", title: "Icon Emoji:", description: "Override the default profile picture", required: false)
    input("debug", "bool", title: "Enable Debug Logging", required: true, defaultValue: false)
}

metadata {
    definition (name: "Mattermost", namespace: "jglemza-hubitat", author: "Joshua Glemza",
               importUrl: "https://git.hwarf.com/jglemza/hubitat/raw/branch/main/drivers/mattermost.groovy") {
        capability "Notification"
        capability "Actuator"
        capability "Speech Synthesis"
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()   
}

def initialize() {
}

def speak(message) {
    deviceNotification(message)
}

def deviceNotification(message) {
  
    def postBody = [text:message]

    if(settings.mm_channel != '') {
        postBody['channel'] = settings.mm_channel
    }
    
    if(settings.mm_username != '') {
        postBody['username'] = settings.mm_username
    }
    
    if(settings.mm_icon_url != '') {
        postBody['icon_url'] = settings.mm_icon_url
    }
    
    if(settings.mm_icon_emoji != '') {
        postBody['icon_emoji'] = settings.mm_icon_emoji
    }
         
    ifDebug("Sending Message: $message [${postBody}]")

    // Prepare the package to be sent
    def params = [
        uri: settings.mm_url,
        body: postBody
    ]
    try {
       httpPostJson(params){response ->
            if(response.status != 202) {
                sendPush("ERROR: 'Mattermost' received HTTP error ${response.status}.")
                log.error "Received HTTP error ${response.status}. ${response}"
            }
            else {
                ifDebug("Message queued for sending via Mattermost.")
       }
      }
    } catch (e) {
        log.error "Mattermost: Error making httpPostJson: $e"
    }
}

private ifDebug(msg) {
    if (settings?.debug || settings?.debug == null) {
        log.debug 'Mattermost: ' + msg
    }
}
