package com.integratorsb2b.ug.login

import com.integratorsb2b.ug.BaseContract


interface LoginContract {

    interface View : BaseContract.View<LoginContract.Presenter> {
        fun showCreateAccount()
        fun showForgotPassword()
    }

    interface Presenter : BaseContract.Presenter {
        fun login()
        fun forgotPassword()
        fun createAccount()
    }
}