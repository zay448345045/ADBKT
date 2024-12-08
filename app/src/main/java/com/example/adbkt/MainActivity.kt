package com.example.adbkt

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.adbkt.utils.ADB

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val adb = ADB.getInstance(applicationContext)
        // Create a ConstraintLayout (or use any other layout as the root)
        val layout = ConstraintLayout(this)
        layout.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )

        // Create a Button programmatically
        val button = Button(this).apply {
            text = "Click Me"
            id = View.generateViewId() // Ensure unique ID
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                marginStart = 32
                topMargin = 32
            }
        }

        layout.addView(button)

        button.setOnClickListener {
            Toast.makeText(this, "Accessibility On...", Toast.LENGTH_SHORT).show()
            adb.sendToShellProcess("settings put secure enabled_accessibility_services com.example.adbkt/com.example.adbkt.MyAccessibilityService");
        }

        setContentView(layout)

        adb.debug("MainActivity Running.....");
    }
}
