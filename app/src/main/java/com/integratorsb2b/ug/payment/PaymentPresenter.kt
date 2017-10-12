package com.integratorsb2b.ug.payment

import android.content.Context
import com.integratorsb2b.ug.Payload


class PaymentPresenter(private val context: Context, private val view: PaymentContract.View) :
        PaymentContract.Presenter {
    private lateinit var localPayload: Payload

    init {
        view.setPresenter(this)
    }

    override fun begin() {
        TODO("not implemented")
    }

    override fun next() {
        view.showConfirmation(localPayload)
    }

    override fun setPayload(payload: Payload) {
        localPayload = payload
    }
}