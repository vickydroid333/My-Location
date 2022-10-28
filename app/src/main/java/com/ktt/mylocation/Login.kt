package com.ktt.mylocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ktt.mylocation.databinding.ActivityLoginBinding
import com.ktt.mylocation.user.MyDataBase
import com.ktt.mylocation.user.MyFactory
import com.ktt.mylocation.user.MyRepository
import com.ktt.mylocation.user.MyViewModel
import kotlinx.coroutines.flow.first

class Login : AppCompatActivity(R.layout.activity_login) {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var dataStore: LoginDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val myRepository = MyRepository(MyDataBase.getDatabase(this))
        val factory1 = MyFactory(myRepository)
        myViewModel = ViewModelProvider(this, factory1)[MyViewModel::class.java]
        dataStore = LoginDataStore(this)

        binding.register.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }


        loginValidation()

    }





    private fun Context.showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loginValidation() {
        binding.apply {

            login.setOnClickListener {
                when {
                    userName.text.toString().isEmpty() -> {
                        showToast(" Enter User Name")
                    }

                    password.text.toString().isEmpty() -> {
                        showToast(" Enter Password")
                    }

                    else -> {
                        lifecycleScope.launchWhenStarted {

                            val userName: String = userName.text.toString().trim()
                            val password = password.text.toString().trim()

                            myViewModel.verifyUserLogin(userName, password)!!.collect {
                                Log.i("TAG", "loginValidation:$it ")

                                // check if location is enabled

                                if (it != null) {
                                    showToast(" Login Successfully ")

                                    dataStore.saveLogin(true)
                                    dataStore.saveUserName(it.user_name)
                                    dataStore.savePassword(it.password)
                                    dataStore.saveId(it.id.toString())
                                    val intent =
                                        Intent(this@Login, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    showToast(" Enter Valid user name password ")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}