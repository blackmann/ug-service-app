package com.integratorsb2b.ug.payment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityPaymentBinding
import com.jaredrummler.materialspinner.MaterialSpinner


class PaymentActivity : AppCompatActivity(), PaymentContract.View {

    override fun showWait() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.VISIBLE

        findViewById<TextView>(R.id.info)
                .setText(R.string.setting_up)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.VISIBLE

        findViewById<Button>(R.id.retry)
                .isEnabled = false
    }

    override fun hideWait() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.GONE
    }

    override fun showConnectionError() {
        Toast.makeText(this, "Connection failed. Please check your network and retry.",
                Toast.LENGTH_LONG).show()

        findViewById<Button>(R.id.retry)
                .visibility = View.VISIBLE

        findViewById<TextView>(R.id.info)
                .setText(R.string.connection_failed)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.GONE

        findViewById<Button>(R.id.retry)
                .isEnabled = true
    }

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

    private lateinit var presenter: PaymentContract.Presenter
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
        Log.i("data", payload.form.toString())
    }


    override fun setPresenter(presenter: PaymentContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        payload = intent.getSerializableExtra(payloadKey) as Payload
        PaymentPresenter(this, this)

        presenter.setPayload(payload)

        val binding: ActivityPaymentBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_payment)

        binding.presenter = presenter as PaymentPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.retry)
                .setOnClickListener { presenter.begin() }
        presenter.begin()
    }

    override fun setPaymentOptions(paymentOptions: ArrayList<String>) {
        val paymentOptionsView: MaterialSpinner =
                findViewById(R.id.payment_methods)

        paymentOptionsView.setItems(paymentOptions)
        paymentOptionsView.setOnItemSelectedListener { _, _, _, item ->
            presenter.setPaymentChoice(item as String)
        }

        presenter.setPaymentChoice(paymentOptions[0])
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}