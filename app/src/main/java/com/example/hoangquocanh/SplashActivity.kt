package com.example.hoangquocanh

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hoangquocanh.data.local.user.getUserInfo
import com.example.hoangquocanh.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            val user = getUserInfo(this)
            if(user!=null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2000) // Delay for 2 seconds
    }
}