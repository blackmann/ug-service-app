package com.integratorsb2b.ug.login

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.createaccount.CreateAccountActivity
import com.integratorsb2b.ug.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(), LoginContract.View {

    lateinit var loginPresenter: LoginContract.Presenter

    override fun setPresenter(presenter: LoginContract.Presenter) {
        loginPresenter = presenter
    }

    override fun showCreateAccount() {
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    override fun showForgotPassword() {
        TODO("not implemented")
    }

    override fun showHome() {
        TODO("not implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenter(LoginPresenter(this, this))

        val binding: ActivityLoginBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.presenter = loginPresenter as LoginPresenter?
    }
}