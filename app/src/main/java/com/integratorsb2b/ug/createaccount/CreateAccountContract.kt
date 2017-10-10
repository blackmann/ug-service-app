package com.integratorsb2b.ug.createaccount

import com.integratorsb2b.ug.BaseContract


interface CreateAccountContract {

    interface View: BaseContract.View<Presenter> {
        fun showHome()
        fun goBack()
    }

    interface Presenter: BaseContract.Presenter {
        fun createAccount()
    }
}