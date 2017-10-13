package com.integratorsb2b.ug.home

import android.content.Context
import android.widget.RadioGroup
import android.widget.Toast
import com.integratorsb2b.ug.R

class HomePresenter(private val context: Context, val view: HomeContract.View): HomeContract.Presenter {
    override fun begin() {
        TODO("not implemented")
    }

    private var checkedId: Int = 0

    override fun applyResit() {
        view.showApplyResit()
    }

    override fun requestTranscript() {
        view.showRequestTranscript()
    }

    override fun next() {
    }

    fun proceed(to: Int) {
        when (to) {
            1 -> applyResit()
            2 -> requestTranscript()
        }
    }

    override fun itemChecked() {
        view.showNextTutorial()
    }

    val onCheckChangedListener: RadioGroup.OnCheckedChangeListener
        = RadioGroup.OnCheckedChangeListener { _, checkedId ->  this.checkedId = checkedId}
}