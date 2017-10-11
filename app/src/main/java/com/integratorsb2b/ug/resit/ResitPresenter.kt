package com.integratorsb2b.ug.resit

import android.content.Context
import com.integratorsb2b.ug.Payload

class ResitPresenter(private val context: Context,
                     private val view: ResitContract.View) : ResitContract.Presenter {
    lateinit var selectedLevel: String
    lateinit var selectedProgramme: String

    override fun setLevel(level: String) {
        selectedLevel = level
    }

    override fun setProgramme(programme: String) {
        selectedProgramme = programme
    }

    override fun next() {
        val payload = Payload("resit")
        view.showPaymentForm(payload)
    }

    override fun begin() {
        view.setProgrammeOptions(arrayOf("Humanities", "Sciences", "Administration", "Agriculture"))
        view.setLevelOptions(arrayOf("Undergraduate", "Post Graduate", "Foreign"))
    }
}