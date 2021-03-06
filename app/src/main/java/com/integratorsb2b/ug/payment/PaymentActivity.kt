package com.integratorsb2b.ug.payment

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityPaymentBinding
import com.integratorsb2b.ug.home.MainActivity
import com.integratorsb2b.ug.notification.NotificationService
import com.jaredrummler.materialspinner.MaterialSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


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
        Toast.makeText(this, "Please enter a valid expiry of your card in the format MM/YY; eg 09/17",
                Toast.LENGTH_LONG).show()
    }

    override fun showCardNumberError() {
        Toast.makeText(this, "Please provide a valid card number",
                Toast.LENGTH_SHORT).show()
    }

    private lateinit var presenter: PaymentContract.Presenter
    private lateinit var payload: Payload
    private var dialog: AlertDialog? = null

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
        val dialogView: View = layoutInflater.inflate(R.layout.payment_confirm, null)

        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(dialogView)
        bottomSheet.show()

        dialogView.findViewById<Button>(R.id.send)
                .setOnClickListener({
                    sendForm(payload)
                    bottomSheet.dismiss()
                })
    }

    private fun sendResitForm(payload: Payload) {
        val requestQueue = Volley.newRequestQueue(this)
        val request = object : StringRequest(Request.Method.POST,
                "https://ugapp-integratorsb2b.herokuapp.com/api/ug/resit/payment",
                { response -> handleResponse(response, "resit") },
                { error -> handleError(error) }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                for (c in payload.form.keys) {
                    params.put(c, payload.form[c].toString())
                }

                Log.i("data", params.toString())
                return params
            }
        }

        dialog = AlertDialog.Builder(this)
                .setView(R.layout.view_sending)
                .create()

        dialog?.show()

        requestQueue.add(request)
    }

    private fun sendTranscriptForm(payload: Payload) {
        val applicationType = HashMap<String, Any?>()
        applicationType.put("serviceCharge", payload.form["serviceCharge"])
        applicationType.put("actualCharge", payload.form["actualCharge"])
        applicationType.put("postalCharge", payload.form["postalCharge"])
        applicationType.put("key", payload.form["key"])

        val applicationTypeList = ArrayList<HashMap<String, Any?>>()
        applicationTypeList.add(applicationType)
        payload.form.put("applicationType", applicationTypeList)

        val retro = Retrofit.Builder().baseUrl("https://ugapp-integratorsb2b.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retro.create(TranscriptEnd::class.java)
                .send(payload.form)
                .enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        handleResponse("", "transcript")
                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        handleError(null)
                    }

                })

        dialog = AlertDialog.Builder(this)
                .setView(R.layout.view_sending)
                .create()

        dialog?.show()
    }

    private fun sendForm(payload: Payload?) {
        if (payload == null) return

        if (payload.type == "resit") {
            sendResitForm(payload)
        } else {
            sendTranscriptForm(payload)
        }
    }

    private fun handleError(error: VolleyError?) {
        dialog?.dismiss()
        Toast.makeText(this, "Connection failed. Please try again.", Toast.LENGTH_SHORT)
                .show()
    }

    private fun handleResponse(response: String, purpose: String) {
        dialog?.dismiss()
        val notificationIntent = Intent(this, NotificationService::class.java)
        notificationIntent.putExtra("indexNumber", payload.form["indexNumber"] as String)
        startService(notificationIntent)

        // show some message
        AlertDialog.Builder(this)
                .setTitle("Request Sent")
                .setMessage("Your $purpose request has been sent. You will receive a notification with " +
                        "your registration code shortly. Thank you.")
                .setPositiveButton(android.R.string.ok, { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    continueHome()
                })
                .create()
                .show()
    }

    private fun continueHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    override fun setPresenter(presenter: PaymentContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        payload = intent.getSerializableExtra(payloadKey) as Payload
        PaymentPresenter(this, this)

        presenter.setPayload(payload)

        Log.i("Payload", payload.form.toString())

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

    interface TranscriptEnd {

        @POST("api/ug/transcript/payment")
        fun send(@Body body: HashMap<String, Any>): Call<Void>
    }
}