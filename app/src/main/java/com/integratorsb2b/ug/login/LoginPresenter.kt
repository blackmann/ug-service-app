package com.integratorsb2b.ug.login

import android.content.Context
import android.databinding.ObservableField
import android.util.Log


class LoginPresenter(var context: Context,
                     var view: LoginContract.View) : LoginContract.Presenter {
    override fun next() {
        TODO("not implemented")
    }

    override fun begin() {
        TODO("not implemented")
    }

    val tag: String = "LoginPresenter"

    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")

    override fun login() {
        val usernameText = username.get()
        val passwordText = password.get()
        Log.i(tag, "Username is $usernameText and password is $passwordText")
    }

    override fun forgotPassword() {
        TODO("not implemented")
    }

    override fun createAccount() {
        view.showCreateAccount()
    }

}