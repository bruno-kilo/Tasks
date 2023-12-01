package com.devmasterteam.tasks.utils

import android.content.Context
import com.devmasterteam.tasks.view.LoginActivity
import com.devmasterteam.tasks.view.MainActivity
import com.devmasterteam.tasks.view.RegisterActivity
import com.devmasterteam.tasks.view.TaskFormActivity

class Navigator(private val context: Context) {

    fun goToRegister() {
        val intent = RegisterActivity.newIntent(context)
        context.startActivity(intent)
    }

    fun goToMain() {
        val intent = MainActivity.newIntent(context)
        context.startActivity(intent)
    }

    fun goToLogin() {
        val intent = LoginActivity.newIntent(context)
        context.startActivity(intent)
    }

    fun goToTaskForm() {
        val intent = TaskFormActivity.newIntent(context)
        context.startActivity(intent)
    }

}