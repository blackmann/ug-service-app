package com.integratorsb2b.ug.transcript

import com.integratorsb2b.ug.BaseContract
import com.integratorsb2b.ug.Payload


interface TranscriptContract {

    interface Presenter: BaseContract.Presenter {
        fun setSelectedLocation(location: String)
        fun setSelectedDeliveryChoice(choice: String)
    }

    interface View: BaseContract.View<Presenter> {
        fun hidePostalForm()
        fun showPostalForm()
        fun showNextTap()
        fun showPayment(payload: Payload? = null)
        fun showFormError()
        fun showPostalAddressNotSpecifiedError()
        fun showWait()
        fun hideWait()
        fun showConnectionError()
        fun setPostalOptions(options: ArrayList<String>)
        fun setDeliveryChoices(locations: ArrayList<String>)
    }
}