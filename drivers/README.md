# Mattermost

This is a quick and dirty driver to send messages to a [Mattermost](https://mattermost.com/) server via an [incoming webhook](https://developers.mattermost.com/integrate/webhooks/incoming/). 

## Requirements

* Mattermost Incoming Webhook URL

## Installation

* Drivers Code -> New Driver -> Import URL: https://github.com/jglemza/hubitat/raw/main/drivers/mattermost.groovy
* Go to Devices section and + Add Device -> Virtual
* Enter a Device Name (e.g., Mattermost hubitat channel)
* Set the Type to Mattermost
* Save Device
* Edit your new device and set its preferences (i.e., Webhook URL)
