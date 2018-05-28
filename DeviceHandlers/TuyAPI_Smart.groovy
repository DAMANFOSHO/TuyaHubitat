import groovy.json.JsonSlurper
/*
TuyAPI SmartPlug Device Handler

Derived from
	TP-Link HS Series Device Handler
	Copyright 2017 Dave Gutheinz
Original smartthings work and node server created by Ben Lawson


Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at:
		http://www.apache.org/licenses/LICENSE-2.0
		
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Supported models and functions:  This device supports smart plugs that use the Tuya Smart Life app

Update History
05-23-2018 - Updated to control all powerstrip plugs from one device. Fixed status response - should now be accurate. Node script update with changes by Dave Gutheinz.
05-22-2018 - Updated to make on off commands work and make compatible with Hubitat
01-04-2018	- Initial release
*/
metadata {
	definition (name: "TuyAPI Smart Plug", namespace: "cwwilson08", author: "Chris Wilson") {
		capability "Refresh"
		capability "Actuator"
        capability "Switch"
    
        
        command "USB"
        attribute "USB", "5"
        command "Plug_1"
        attribute "Plug_1", "1"
        command "Plug_2"
        attribute "Plug_2", "2"
        command "Plug_3"
        attribute "Plug_3", "3"
        command "Plug_4"
        attribute "Plug_4", "4"
        command "ALLon"
        attribute "ALLon", "6"
        command "ALLoff"
        attribute "ALLoff", "6"
        
	}
}
preferences {
	input(name: "deviceIP", type: "text", title: "Device IP", required: true, displayDuringSetup: true)
	input(name: "gatewayIP", type: "text", title: "Gateway IP", required: true, displayDuringSetup: true)
	input(name: "deviceID", type: "text", title: "Device ID", required: true, displayDuringSetup: true)
	input(name: "localKey", type: "text", title: "Local Key", required: true, displayDuringSetup: true)
    input(name: "usr", type: "text", title: "Master", required: true, displayDuringSetup: true)
   
}

def installed() {
	updated()
    
}

def updated() {
	unschedule()
	runEvery15Minutes(refresh)
	runIn(2, refresh)
}
//	----- BASIC PLUG COMMANDS ------------------------------------


def on(deviceNo) {
    deviceNo = usr
    sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
   }

def off(deviceNo) {
    deviceNo = usr
    sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
   }

def Plug_1(deviceNo) {
    deviceNo = "Plug_1"

    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}
def Plug_2(deviceNo) {
    deviceNo = "Plug_2"

    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}
def Plug_3(deviceNo) {
    deviceNo = "Plug_3"

    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}
def Plug_4(deviceNo) {
    deviceNo = "Plug_4"

    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}
def USB(deviceNo) {
    deviceNo = "USB"

    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse")
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}

def ALLon(deviceNo) {
    deviceNo = "ALLon"
//	deleted the cmds on and off.  Replace with this.
//	assume that states are on or off.  Offline will try to turn on.
//	added deviceNo.  This will pass through the system to the response method and allow proper parsing.
    def plugState = device.currentValue(deviceNo)
    log.debug "${plugState} LINE 75"
    if (plugState == "on") {
		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse",)
    } else {
		sendCmdtoServer("on", deviceNo, "deviceCommand", "onOffResponse")
    }
}
def ALLoff(deviceNo) {
    deviceNo = "ALLon"

		sendCmdtoServer("off", deviceNo, "deviceCommand", "onOffResponse",)
 
}
def onOffResponse(response, deviceNo, cmdResponse){
    log.debug "at onoff response line 175 ${cmdResponse} and response is ${response} and deviceNo is ${deviceNo}"

	if (cmdResponse == "0") {
		log.error "$device.name $device.label: Communications Error"
       
	
	} else {
        
		log.info "${device.name} ${device.label}: Power: ${response}"
		sendEvent(name: "${deviceNo}", value: response)
        if (response == "off") {
            	sendEvent(name: "ALLon", value: "off")
                runIn(1, refresh)
        } else {
        if (response == "on") 
            	runIn(1, refresh)  
    	}       
	}
}
//	----- REFRESH ------------------------------------------------
def refresh(deviceNo){
    sendCmdtoServer("statusAll", deviceNo, "deviceCommand", "refreshResponse2")
}

def refreshResponse2(cmdResponse, deviceNo){
    log.debug "i am at refresh 2 cmdresponse ${cmdResponse.dps["1"]}"
    log.debug "line 203 ${deviceNo} dps"
  
    if (cmdResponse.dps["1"] == true) {
        status1 = "on"
    } else {
        status1 = "off"
    }  
    if (cmdResponse.dps["2"] == true) {
        status2 = "on"
    } else {
        status2 = "off"
    }
    if (cmdResponse.dps["3"] == true) {
        status3 = "on"
    } else {
        status3 = "off"
    }
    if (cmdResponse.dps["4"] == true) {
        status4 = "on"
    } else {
        status4 = "off"
    }
    if (cmdResponse.dps["5"] == true) {
        status5 = "on"
    } else {
        status5 = "off"
    }
   
    
    
log.debug "${status1} status 1 result line 251"
	    sendEvent(name: "Plug_1", value: status1)
        sendEvent(name: "Plug_2", value: status2)
        sendEvent(name: "Plug_3", value: status3)
    	sendEvent(name: "Plug_4", value: status4)
    	sendEvent(name: "USB", value: status5)
    
    
    if(status1 == "on" && status2 == "on"  && status3 == "on" && status4 == "on" && status5 == "on") {
    log.debug "${status1} and ${status2} and ${status3} and ${status4}"
       sendEvent(name: "ALLon", value: "on")
		log.info "${device.name} ${device.label}: Power: ${status}"
    }
	}

//	----- SEND COMMAND DATA TO THE SERVER -------------------------------------

private sendCmdtoServer(command, deviceNo, hubCommand, action){

    def dps 
    switch(deviceNo) {
        case "Plug_1":
    		dps = 1
        	break
        case "Plug_2":
    		dps = 2
        	break
        case "Plug_3":
    		dps = 3
        	break
        case "Plug_4":
    		dps = 4
        	break
         case "USB":
    		dps = 5
            break
         case "ALLon":
    		dps = 6
        	break
        
        default:
            break
    }
	def headers = [:] 
	headers.put("HOST", "$gatewayIP:8083")	//	SET TO VALUE IN JAVA SCRIPT PKG.
	headers.put("tuyapi-ip", deviceIP)
	headers.put("tuyapi-devid", deviceID)
	headers.put("tuyapi-localkey", localKey)
	headers.put("tuyapi-command", command)
    headers.put("action", action)
	headers.put("command", hubCommand)
    headers.put("deviceNo", deviceNo)
    headers.put("dps", dps)
    headers.put("scheme", true)
   
	def hubCmd = new hubitat.device.HubAction([
        method: "GET",
		headers: headers]
    
	)
    log.debug "${hubCmd}"
    hubCmd
    
}

def parse(response) {

	def resp = parseLanMessage(response)
	def action = resp.headers["action"]
    def deviceNo = resp.headers["deviceNo"]
	def jsonSlurper = new JsonSlurper()
	def cmdResponse = jsonSlurper.parseText(resp.headers["cmd-response"])
    def onoff = resp.headers["onoff"]
log.error onoff
log.error deviceNo
log.error cmdResponse
    
    if (cmdResponse == "TcpTimeout") {
		log.error "$device.name $device.label: Communications Error"
		sendEvent(name: "switch", value: "offline", descriptionText: "ERROR at hubResponseParse TCP Timeout")
		sendEvent(name: "deviceError", value: "TCP Timeout in Hub")
	} else {
        log.debug "line 175 ${action} and ${cmdResponse}"
		actionDirector(action, cmdResponse, onoff, deviceNo)
		sendEvent(name: "deviceError", value: "OK")
	}
}
def actionDirector(action, cmdResponse, onoff, deviceNo) {
	switch(action) {
		case "onOffResponse":
        onOffResponse(onoff, deviceNo, cmdResponse)
			break

		
        
        case "refreshResponse2":
        log.debug "line 188 ${cmdResponse}"
			refreshResponse2(cmdResponse, deviceNo)
			break

		default:
			log.debug "at default"
	}
}
