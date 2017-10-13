package com.integratorsb2b.ug.payment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.Payload.PaymentOptions.Companion.airtelMoney
import com.integratorsb2b.ug.Payload.PaymentOptions.Companion.masterCard
import com.integratorsb2b.ug.Payload.PaymentOptions.Companion.mtnMomo
import com.integratorsb2b.ug.Payload.PaymentOptions.Companion.tigoCash
import com.integratorsb2b.ug.Payload.PaymentOptions.Companion.visa
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.confirmation.ConfirmationActivity
import com.integratorsb2b.ug.databinding.ActivityPaymentBinding
import com.jaredrummler.materialspinner.MaterialSpinner


class PaymentActivity : AppCompatActivity(), PaymentContract.View {
    override fun showCvvError() {
        Toast.makeText(this, "Please enter a valid cvv. It can be found at the back of your card.", Toast.LENGTH_SHORT)
                .show()
    }

    override fun showPhoneNumberError() {
        Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT)
                .show()
    }

    override fun showExpiryError() {
        Toast.makeText(this, "Please enter the expiry of your card in the format MM/YY; eg 09/17",
                Toast.LENGTH_LONG).show()
    }

    override fun showCardNumberError() {
        Toast.makeText(this, "Please provide a valid card number",
                Toast.LENGTH_SHORT).show()
    }

    private lateinit var localPresenter: PaymentContract.Presenter
    private lateinit var payload: Payload

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

    override fun showReceipt() {

    }


    override fun setPresenter(presenter: PaymentContract.Presenter) {
        localPresenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        payload = intent.getSerializableExtra(payloadKey) as Payload
        PaymentPresenter(this, this)

        localPresenter.setPayload(payload)

        val binding: ActivityPaymentBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_payment)

        binding.presenter = localPresenter as PaymentPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setPaymentOptions()
    }

    private fun setPaymentOptions() {
        val paymentOptionsView: MaterialSpinner =
                findViewById(R.id.payment_methods)

        val paymentOptions = arrayListOf(mtnMomo, airtelMoney, tigoCash,
                visa, masterCard)
        paymentOptionsView.setItems(paymentOptions)
        paymentOptionsView.setOnItemSelectedListener { _, _, _, item ->
            localPresenter.setPaymentChoice(item as String)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}