package com.integratorsb2b.ug.transcript

import com.integratorsb2b.ug.BaseContract
import com.integratorsb2b.ug.Payload


interface TranscriptContract {

    interface Presenter: BaseContract.Presenter {

    }

    interface View: BaseContract.View<Presenter> {
        fun hidePostalForm()
        fun showPostalForm()
        fun showNextTap()
        fun showPaymentForm(payload: Payload? = null)
    }
}