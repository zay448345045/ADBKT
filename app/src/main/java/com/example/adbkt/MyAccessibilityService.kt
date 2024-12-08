package com.example.adbkt;
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.adbkt.utils.ADB

class MyAccessibilityService : AccessibilityService() {

    var port = "";
    var server = "";
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // Get the root node from the event's source
            val rootNode = it.source
            rootNode?.let { node ->
                // Process the root node
                processNode(node)
            }
        }
    }

    // Function to recursively process each node

    fun processNode(node: AccessibilityNodeInfo) {
        // Get the current node's text or other details
        val text = node.text?.toString() ?: "No text"
        val className = node.className?.toString() ?: "No class name"
        val contentDescription = node.contentDescription?.toString() ?: "No description"

        if(text.length == 6 || text.contains((':'))) {
            // Log or handle the node details
            Log.d("AccessibilityNode", "Text: $text, Class: $className, Description: $contentDescription")
            if (text.length == 6) {
                port = text
            }
            if (text.contains(':')) {
                server = text
            }
            if (server != "" && port != "") {
                val adb = ADB.getInstance(applicationContext)
                adb.pair(server.split(":")[1], port);
                adb.initServer()
            }
            // Now, process any child nodes of this node
            val childCount = node.childCount
            for (i in 0 until childCount) {
                node.getChild(i)?.let { child ->
                    processNode(child)
                }
            }
        }

    }

    override fun onInterrupt() {
        // Handle interruptions
    }
    override fun onServiceConnected() {
        super.onServiceConnected()

        // Create an instance of AccessibilityServiceInfo
        val info = AccessibilityServiceInfo().apply {
            // Set the event types you want to listen for
            eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or AccessibilityEvent.TYPE_VIEW_CLICKED

            // Set the feedback type (what type of feedback the service provides)
            feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN

            // Set flags for the service
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS

            // Set can retrieve window content flag
            //canRetrieveWindowContent = true
        }

        // Assign the constructed AccessibilityServiceInfo object to serviceInfo
        serviceInfo = info
    }


}
