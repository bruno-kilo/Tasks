package com.devmasterteam.tasks.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.utils.Navigator
import com.devmasterteam.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    // Coloquei o null safety, estava com lateinit


    private var viewModel: LoginViewModel? = null
    private var binding: ActivityLoginBinding? = null
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        // Layout
        setContentView(binding?.root)

        initialize()
    }

    private fun initialize() {
        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Eventos
        binding?.apply {
            buttonLogin.setOnClickListener { handleLogin() }
            textRegister.setOnClickListener {
                navigator?.goToRegister()
            }
        }

        viewModel?.verifyLoggedUser()

        // Observadores
        observe()

        supportActionBar?.hide()
    }

    private fun observe() {
        viewModel?.apply {
            login.observe(this@LoginActivity) {
                if (it.status()) {
                    navigator?.goToMain()
                    finish()
                } else {
                    Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
                }
            }
            loggedUser.observe(this@LoginActivity) {
                if (it) {
                    navigator?.goToMain()
                    finish()
                }
            }
        }
    }

    private fun handleLogin() {
        binding?.apply {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            viewModel?.doLogin(email, password)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}