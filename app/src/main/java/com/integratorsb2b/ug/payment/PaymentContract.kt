package com.integratorsb2b.ug.payment

import com.integratorsb2b.ug.BaseContract


interface PaymentContract {

    interface Presenter: BaseContract.Presenter {

    }

    interface View: BaseContract.View<Presenter> {
        fun showMomoForm()
        fun showCardForm()
        fun showConfirmation()
    }
}