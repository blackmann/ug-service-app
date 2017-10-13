package com.integratorsb2b.ug.payment

import android.content.Context
import android.databinding.ObservableField
import android.util.Log
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.integratorsb2b.ug.Payload
import java.util.regex.Pattern


class PaymentPresenter(private val context: Context, private val view: PaymentContract.View) :
        PaymentContract.Presenter {
    private lateinit var payload: Payload

    private lateinit var paymentChoice: String

    private lateinit var paymentOptions: ArrayList<PaymentOption>

    var cardNumber: ObservableField<String> = ObservableField()
    var cvv: ObservableField<String> = ObservableField()
    var expiry: ObservableField<String> = ObservableField()

    var phoneNumber: ObservableField<String> = ObservableField()

    init {
        view.setPresenter(this)
    }

    override fun begin() {
        getPaymentOptions()
    }

    private fun getPaymentOptions() {
        view.showWait()
        val requestQueue = Volley.newRequestQueue(context)

        val request = StringRequest(Request.Method.GET, "https://ugapp-integratorsb2b.herokuapp.com/api/ug/paymentoption",
                {response -> handleResponse(response) },
                {error -> handleError(error)})

        requestQueue.add(request)
    }

    private fun handleResponse(response: String) {
        view.hideWait()
        paymentOptions = Gson().fromJson(response)

        // now inform the view
        val options: ArrayList<String> = ArrayList()
        for (p in paymentOptions) {
            options.add(p.paymentOptionName[0].key)
        }

        view.setPaymentOptions(options)
    }

    private fun handleError(error: VolleyError) {
        view.showConnectionError()
    }

    override fun next() {
        when (paymentChoice.toLowerCase()) {
            Payload.PaymentOptions.visa,
            Payload.PaymentOptions.masterCard -> processCardForm(paymentChoice)

            else -> processMomoForm(paymentChoice)
        }
    }

    private fun processCardForm(paymentChoice: String) {
        if (!isValidCardNumber()) {
            view.showCardNumberError()
            return
        }

        if (!isValidExpiry()) {
            view.showExpiryError()
            return
        }

        if (!isValidCvv()) {
            view.showCvvError()
            return
        }

        val cardNumber: String = this.cardNumber.get()
        val cvv: String = this.cvv.get()
        val expiry: String = this.expiry.get()

        payload.form.put("paymentMethod", getPaymentCode(paymentChoice))
        payload.form.put("cardNumber", cardNumber)
        payload.form.put("cvv", cvv)
        payload.form.put("expiryDate", expiry)

        view.showReceipt()
    }

    private fun getPaymentCode(paymentChoice: String): String {
        for (p in paymentOptions) {
            val paymentName = p.paymentOptionName[0]
            if (paymentName.key == paymentChoice) {
                return paymentName.value
            }
        }

        return ""
    }

    private fun isValidCvv(): Boolean {
        return cvv.get() != null && cvv.get().trim().length == 3
    }

    private fun isValidCardNumber(): Boolean {
        return cardNumber.get() != null && cardNumber.get().trim().length > 9
    }

    private fun isValidExpiry(): Boolean {
        return expiry.get() != null && Pattern.matches("\\d{2}/\\d{2}", expiry.get())
    }

    private fun processMomoForm(paymentChoice: String) {
        if (phoneNumber.get() == null || phoneNumber.get().trim().length != 10) {
            view.showPhoneNumberError()
            return
        }

        payload.form.put("phoneNumber", phoneNumber.get())
        payload.form.put("paymentMethod", getPaymentCode(paymentChoice))

        view.showReceipt()
    }

    override fun setPayload(payload: Payload) {
        this.payload = payload
    }

    override fun setPaymentChoice(choice: String) {
        paymentChoice = choice
        when (choice.toLowerCase()) {
            Payload.PaymentOptions.visa,
            Payload.PaymentOptions.masterCard -> view.showCardForm()

            else -> view.showMomoForm()
        }
    }

    class PaymentOptionName(val value: String, val key: String)

    class PaymentOption(val paymentOptionName: ArrayList<PaymentOptionName>)
}