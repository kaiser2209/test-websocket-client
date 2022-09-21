package com.example.testwebsocketclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Functions.startWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()

        Functions.closeClient()
    }


}