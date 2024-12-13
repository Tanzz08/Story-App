package com.example.storyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storyapp.viewmodel.AuthViewModel
import com.example.storyapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {


    private val viewModel by viewModels<AuthViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            delay(2000)
            val token = viewModel.getToken().first()

            if (token.isNotEmpty()){
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            }else {
                startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java))

            }
            finish()
        }

    }
}