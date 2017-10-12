package com.integratorsb2b.ug.payment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.confirmation.ConfirmationActivity
import com.integratorsb2b.ug.databinding.ActivityPaymentBinding
import com.jaredrummler.materialspinner.MaterialSpinner


class PaymentActivity : AppCompatActivity(), PaymentContract.View {

    private lateinit var localPresenter: PaymentContract.Presenter

    companion object {
        val payloadKey = "ug_payload_key"

        fun start(context: Context, payload: Payload? = null) {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(payloadKey, payload)
            context.startActivity(intent)
        }
    }


    override fun showMomoForm() {
        findViewById<LinearLayout>(R.id.mobile_money_form)
                .visibility = View.VISIBLE

        findViewById<LinearLayout>(R.id.card_form)
                .visibility = View.GONE
    }

    override fun showCardForm() {
        findViewById<LinearLayout>(R.id.mobile_money_form)
                .visibility = View.GONE

        findViewById<LinearLayout>(R.id.card_form)
                .visibility = View.VISIBLE
    }

    override fun showConfirmation(payload: Payload) {
        ConfirmationActivity.start(this, payload)
    }


    override fun setPresenter(presenter: PaymentContract.Presenter) {
        localPresenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val payload = intent.getSerializableExtra(payloadKey) as Payload?
        PaymentPresenter(this, this)
        if (payload != null) {
            localPresenter.setPayload(payload)
        }
        val binding: ActivityPaymentBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_payment)

        binding.presenter = localPresenter as PaymentPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPaymentOptions()
    }

    private fun setPaymentOptions() {
        val paymentOptionsView: MaterialSpinner =
                findViewById(R.id.payment_methods)

        val paymentOptions = arrayListOf("MTN Mobile Money", "Airtel Money", "Tigo Cash",
                "VISA", "Mastercard")
        paymentOptionsView.setItems(paymentOptions)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}