package com.devmasterteam.tasks.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.utils.Navigator
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private var viewModel: RegisterViewModel? = null
    private var binding: ActivityRegisterBinding? = null
    private var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initialize()
    }

    private fun initialize() {
        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Eventos
        binding?.buttonSave?.setOnClickListener { handleSave() }

        observe()
    }

    private fun observe() {
        viewModel?.user?.observe(this) {
            if (it.status()) {
                navigator?.goToLogin()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSave() {
        binding?.apply {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            viewModel?.create(name, email, password)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }
}