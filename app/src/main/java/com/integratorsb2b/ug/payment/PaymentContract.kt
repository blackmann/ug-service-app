package com.integratorsb2b.ug.payment

import com.integratorsb2b.ug.BaseContract
import com.integratorsb2b.ug.Payload


interface PaymentContract {

    interface Presenter: BaseContract.Presenter {
        fun setPayload(payload: Payload)
        fun setPaymentChoice(choice: String)
    }

    interface View: BaseContract.View<Presenter> {
        fun showMomoForm()
        fun showCardForm()
        fun showReceipt()
        fun showPhoneNumberError();
        fun showExpiryError()
        fun showCardNumberError()
        fun showCvvError()
        fun showWait()
        fun hideWait()
        fun showConnectionError()
        fun setPaymentOptions(paymentOptions: ArrayList<String>)
    }
}