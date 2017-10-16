package com.integratorsb2b.ug.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.payment.PaymentActivity


class ConfirmationActivity : AppCompatActivity() {
    private lateinit var payload: Payload
    private var serviceCharge: Double = 0.0
    private var totalAmount: Double = 0.0

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
        else if (payload.type == "resit") {
            fragment = ResitConfirmationFragment.getInstance(payload)
            loadServiceCharge()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

        setFields()

        findViewById<Button>(R.id.retry)
                .setOnClickListener { loadServiceCharge() }

        findViewById<FloatingActionButton>(R.id.done)
                .setOnClickListener { showPaymentScreen() }
    }

    private fun showLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.VISIBLE

        findViewById<Button>(R.id.retry)
                .isEnabled = false

        findViewById<TextView>(R.id.info)
                .setText(R.string.setting_up)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.VISIBLE

        findViewById<FloatingActionButton>(R.id.done)
                .visibility = View.GONE

        findViewById<Button>(R.id.retry)
                .isEnabled = false
    }

    private fun showError() {
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

    private fun hideLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.GONE

        findViewById<FloatingActionButton>(R.id.done)
                .visibility = View.VISIBLE
    }

    private fun loadServiceCharge() {
        val requestQueue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.POST,
                "https://ugapp-integratorsb2b.herokuapp.com/api/ug/resit/charges",
                { response -> handleResponse(response) },
                { error -> handleError(error) }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("multiplier", (payload.form["creditHours"] as Int).toString())

                return params
            }
        }

        showLoading()
        requestQueue.add(request)
    }

    private fun handleError(error: VolleyError) {
        showError()
    }

    private fun handleResponse(response: String) {
        hideLoading()

        val data: HashMap<String, Double> = Gson().fromJson(response)
        val serviceChargeTemp = data["serviceCharge"]
        if (serviceChargeTemp != null) {
            serviceCharge = serviceChargeTemp
            updateServiceCharge()
        }
    }

    private fun updateServiceCharge() {
        setGrandTotal()
        setServiceCharge()
    }

    private fun setServiceCharge() {
        findViewById<TextView>(R.id.service_charge)
                .setText(String.format("GHS %.2f", serviceCharge))
    }

    private fun showPaymentScreen() {
        payload.form.put("serviceCharge", serviceCharge)
        payload.form.put("totalAmount", totalAmount+serviceCharge)
        PaymentActivity.start(this, payload)
    }

    private fun setFields() {
        // set the student number
        val studentNumber: String = payload.form["indexNumber"] as String
        findViewById<TextView>(R.id.student_number)
                .setText(studentNumber)

        when (payload.type) {
            "transcript" -> setTranscriptValues()
            "resit" -> setResitValues()
        }

        updateServiceCharge()
    }

    private fun setPurpose(purposeString: Int) {
        val purposeView = findViewById<TextView>(R.id.purpose)
        purposeView.setText(purposeString)
    }

    private fun setTranscriptValues() {
        setPurpose(R.string.request_for_transcript)
        serviceCharge = payload.form["serviceCharge"] as Double

        val actualCharge: Double = payload.form["actualCharge"] as Double
        val postalCharge: Double = payload.form["postalCharge"] as Double

        totalAmount = actualCharge + postalCharge

        setTotalAmount()
        updateServiceCharge()
        setGrandTotal()
    }

    private fun setTotalAmount() {
        findViewById<TextView>(R.id.total_amount)
                .setText(String.format("GHS %.2f", totalAmount))
    }

    private fun setResitValues() {
        setPurpose(R.string.apply_for_resit)
        val charge = payload.form["charge"] as Double
        val creditHours = payload.form["creditHours"] as Int

        totalAmount = charge * creditHours
        setTotalAmount()

        setGrandTotal()
    }

    private fun setGrandTotal() {
        val grand: Double = totalAmount + serviceCharge

        findViewById<TextView>(R.id.grand_total)
                .setText(String.format("GHS %.2f", grand))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}