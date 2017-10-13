package com.integratorsb2b.ug.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.payment.PaymentActivity


class ConfirmationActivity : AppCompatActivity() {
    private lateinit var payload: Payload

    companion object {
        val payloadExtra = "ug_payload_extra"

        fun start(context: Context, payload: Payload? = null) {
            val intent = Intent(context, ConfirmationActivity::class.java)
            intent.putExtra(payloadExtra, payload)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        payload = intent.getSerializableExtra(payloadExtra) as Payload

        var fragment: Fragment? = null
        if (payload.type == "transcript") fragment =
                TranscriptConfirmationFragment.getInstance(payload)
        else if (payload.type == "resit") fragment =
                ResitConfirmationFragment.getInstance(payload)

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

        setFields()

        findViewById<FloatingActionButton>(R.id.done)
                .setOnClickListener { showPaymentScreen() }
    }

    private fun showPaymentScreen() {
        PaymentActivity.start(this, payload)
    }

    private fun setFields() {
        // set the student number
        val studentNumber: String = payload.form["studentNumber"] as String
        findViewById<TextView>(R.id.student_number)
                .setText(studentNumber)

        // set the purpose
        val purposeView = findViewById<TextView>(R.id.purpose)
        when (payload.type) {
            "transcript" -> setTranscriptValues()
            "resit" -> setResitValues()
        }
    }

    private fun setPurpose(purposeString: Int) {
        val purposeView = findViewById<TextView>(R.id.purpose)
        purposeView.setText(purposeString)
    }

    private fun setTranscriptValues() {
        setPurpose(R.string.request_for_transcript)
    }

    private fun setResitValues() {
        setPurpose(R.string.apply_for_resit)
        val charge = payload.form["charge"] as Double
        val creditHours = payload.form["creditHours"] as Int

        val totalAmount: Double = charge * creditHours
        findViewById<TextView>(R.id.total_amount)
                .setText(String.format("GHS %.2f", totalAmount))

        setGrandTotal(totalAmount)
    }

    private fun setGrandTotal(totalAmount: Double) {
        val grand: Double = totalAmount + getServiceCharge()

        findViewById<TextView>(R.id.grand_total)
                .setText(String.format("GHS %.2f", grand))
    }

    private fun getServiceCharge(): Double {
        return 1.20
        // TODO this needs the right implementation
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}