package com.ktt.mylocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ktt.mylocation.databinding.ActivityRegisterBinding
import com.ktt.mylocation.user.*

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myRepository = MyRepository(MyDataBase.getDatabase(this))
        val factory = MyFactory(myRepository)
        myViewModel = ViewModelProvider(this, factory)[MyViewModel::class.java]

        binding.login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        registerValidation()
    }

    private fun Context.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun registerValidation() {
        binding.apply {
            register.setOnClickListener {
                when {
                    userName.text.toString().isEmpty() -> {
                        showToast("Enter Username")
                    }
                    email.text.toString().isEmpty() -> {
                        showToast("Enter Email Address")
                    }
                    password.text.toString().isEmpty() -> {
                        showToast("Enter Password")
                    }
                    confirmPassword.text.toString().isEmpty() -> {
                        showToast("Enter confirm Password")
                    }

                    else -> {

                        lifecycleScope.launchWhenStarted {
                            val userName = userName.text.toString().trim()
                            val email = email.text.toString().trim()
                            val password = password.text.toString().trim()
                            val confirmPassword = confirmPassword.text.toString().trim()

                            myViewModel.findByEmail(userName)!!.collect {
                                Log.i("TAG", "registerValidation:$it ")

                                if (it == null) {
                                    myViewModel.register(
                                        MyTable(
                                            userName,
                                            email,
                                            password,
                                            confirmPassword
                                        )

                                    )
                                    showToast(" Register Successfully ")
                                    binding.userName.setText("")
                                    binding.email.setText("")
                                    binding.password.setText("")
                                    binding.confirmPassword.setText("")

                                    val intent = Intent(this@Register, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
