package com.ktt.mylocation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ktt.mylocation.databinding.ActivitySplashBinding
import kotlinx.coroutines.flow.first

class Splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var dataStore: LoginDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStore = LoginDataStore(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val animation = AnimationUtils.loadAnimation(this, R.anim.launcher_icon)
        binding.logo.startAnimation(animation)

        Handler().postDelayed({
          userLogin()
        }, 3000)
    }

    private fun userLogin() {
        lifecycleScope.launchWhenStarted {
            val isLog = dataStore.isLoggedIn().first()
            if (isLog) {
                val intent = Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                val intent = Intent(this@Splash, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}