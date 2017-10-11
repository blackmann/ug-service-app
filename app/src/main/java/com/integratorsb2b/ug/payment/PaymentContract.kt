package com.integratorsb2b.ug.payment

import com.integratorsb2b.ug.BaseContract
import com.integratorsb2b.ug.Payload


interface PaymentContract {

    interface Presenter: BaseContract.Presenter {
        fun setPayload(payload: Payload)
    }

    interface View: BaseContract.View<Presenter> {
        fun showMomoForm()
        fun showCardForm()
        fun showConfirmation(payload: Payload)
    }
}