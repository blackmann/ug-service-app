package com.integratorsb2b.ug.transcript

import android.content.Context
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioGroup
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.Util


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

        if (!Util.isValidStudentNumber(studentNumber) || deliveryChoice == 0) {
            view.showFormError()
            return
        }
        // check for the delivery
        var deliveryType = "pickup"
        if (deliveryChoice == R.id.post) {
            if (!Util.isValidPostalAddress(postalAddress)) {
                view.showPostalAddressNotSpecifiedError()
                return
            }

            deliveryType = "delivery"
        }

        payload.form.put("studentNumber", studentNumber.get().trim())
        payload.form.put("deliveryChoice", deliveryType)

        if (deliveryChoice == R.id.post) {
            payload.form.put("postalAddress", postalAddress.get())
        }
        view.showConfirmation(payload)
    }

    private fun setDeliveryChoice(checkedId: Int) {
        deliveryChoice = checkedId
        if (checkedId == R.id.post) view.showPostalForm()
        else view.hidePostalForm()

        // show the tap
        shouldTap()
    }

    private fun shouldTap() {
        val postalAddress = this.postalAddress.get()

        if (!Util.isValidStudentNumber(this.studentNumber)) {
            return
        }

        if (deliveryChoice == R.id.post && postalAddress == null) {
            return
        }

        val studentNumber = this.studentNumber.get()
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