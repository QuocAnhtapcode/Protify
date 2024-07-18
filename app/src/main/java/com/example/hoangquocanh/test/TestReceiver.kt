package com.example.hoangquocanh.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TestReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_SCREEN_OFF){
            //Log.e("TestReceiver","Screen off")
        }
    }
}