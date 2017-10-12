package com.integratorsb2b.ug.payment

import android.content.Context
import android.databinding.ObservableField
import com.integratorsb2b.ug.Payload
import java.util.regex.Pattern


class PaymentPresenter(private val context: Context, private val view: PaymentContract.View) :
        PaymentContract.Presenter {
    private lateinit var payload: Payload

    private var paymentChoice: String = Payload.PaymentOptions.mtnMomo // default

    var cardNumber: ObservableField<String> = ObservableField()
    var cvv: ObservableField<String> = ObservableField()
    var expiry: ObservableField<String> = ObservableField()

    var phoneNumber: ObservableField<String> = ObservableField()

    init {
        view.setPresenter(this)
    }

    override fun begin() {
        TODO("not implemented")
    }

    override fun next() {
        when (paymentChoice) {
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

        payload.form.put("payment_method", paymentChoice)
        payload.form.put("card_number", cardNumber)
        payload.form.put("cvv", cvv)
        payload.form.put("expiry", expiry)

        view.showConfirmation()
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

        payload.form.put("mobile_number", phoneNumber.get())
        payload.form.put("payment_method", paymentChoice)

        view.showConfirmation()
    }

    override fun setPayload(payload: Payload) {
        this.payload = payload
    }

    override fun setPaymentChoice(choice: String) {
        paymentChoice = choice
        when (choice) {
            Payload.PaymentOptions.visa,
            Payload.PaymentOptions.masterCard -> view.showCardForm()

            else -> view.showMomoForm()
        }
    }
}