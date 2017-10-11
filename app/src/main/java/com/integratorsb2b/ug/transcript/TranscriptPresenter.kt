package com.integratorsb2b.ug.transcript

import android.content.Context
import com.integratorsb2b.ug.Payload


class TranscriptPresenter(private val context: Context,
                          private val view: TranscriptContract.View): TranscriptContract.Presenter {
    override fun begin() {
        TODO("not implemented")
    }

    override fun next() {
        val payload = Payload("transcript")
        view.showPaymentForm(payload)
    }
}