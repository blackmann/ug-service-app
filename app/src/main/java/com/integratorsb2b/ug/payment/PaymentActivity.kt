package com.integratorsb2b.ug.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.integratorsb2b.ug.Payload


class PaymentActivity : AppCompatActivity(), PaymentContract.View {

    companion object {
        val payloadKey = "ug_payload_key"

        fun start(context: Context, payload: Payload? = null) {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(payloadKey, payload)
            context.startActivity(intent)
        }
    }


    override fun showMomoForm() {
        TODO("not implemented")
    }

    override fun showCardForm() {
        TODO("not implemented")
    }

    override fun showConfirmation() {
        TODO("not implemented")
    }


    override fun setPresenter(presenter: PaymentContract.Presenter) {
        TODO("not implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val payload = intent.getSerializableExtra(payloadKey) as Payload?
    }
}