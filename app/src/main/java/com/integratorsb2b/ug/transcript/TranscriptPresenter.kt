package com.integratorsb2b.ug.transcript

import android.content.Context
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import android.widget.Toast
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R


class TranscriptPresenter(private val context: Context,
                          private val view: TranscriptContract.View) : TranscriptContract.Presenter {

    private var deliveryChoice: Int = 0
    var studentNumber: ObservableField<String> = ObservableField()
    var postalAddress: ObservableField<String> = ObservableField()
    private var alreadyShown = false

    init {
        view.setPresenter(this)
    }

    override fun begin() {
        TODO("not implemented")
    }

    override fun next() {
        val payload = Payload("transcript")

        if (!isValidStudentNumber() || deliveryChoice == 0) {
            view.showFormError()
            return
        }
        // check for the delivery
        var deliveryType = "pickup"
        if (deliveryChoice == R.id.post && !isValidPostalAddress()) {
            view.showPostalAddressNotSpecifiedError()
            return
        } else deliveryType = "delivery"

        payload.form.put("student_number", studentNumber.get().trim())
        payload.form.put("deliveryChoice", deliveryType)

        if (deliveryChoice == R.id.post) {
            payload.form.put("postal_address", postalAddress.get())
        }
        view.showPaymentForm(payload)
    }

    private fun setDeliveryChoice(checkedId: Int) {
        deliveryChoice = checkedId
        if (checkedId == R.id.post) view.showPostalForm()
        else view.hidePostalForm()

        // show the tap
        shouldTap()
    }

    private fun isValidPostalAddress(): Boolean {
        return postalAddress.get() != null && postalAddress.get().trim().length > 5
    }

    private fun isValidStudentNumber(): Boolean {
        return studentNumber.get() != null && studentNumber.get().trim().length > 4
    }

    private fun shouldTap() {
        val studentNumber = this.studentNumber.get()
        val postalAddress = this.postalAddress.get()

        if (!isValidStudentNumber()) {
            return
        }

        if (deliveryChoice == R.id.post && postalAddress == null) {
            return
        }

        // showing tap is reliant on the student number being supplied
        if (studentNumber.length > 3) {
            when (deliveryChoice) {
                R.id.post -> confirmShowTap(postalAddress.length > 4)
                R.id.pickup -> confirmShowTap(true)
            }
        }
    }

    private fun confirmShowTap(show: Boolean) {
        if (show && !alreadyShown) {
            view.showNextTap()
            alreadyShown = true
        }
    }

    val deliveryChoiceListener: RadioGroup.OnCheckedChangeListener =
            RadioGroup.OnCheckedChangeListener { _, checkedId -> setDeliveryChoice(checkedId) }

    val onTextChanged: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            shouldTap()
        }

    }
}