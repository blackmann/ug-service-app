package com.integratorsb2b.ug.home

import android.content.Context
import android.widget.RadioGroup
import android.widget.Toast
import com.integratorsb2b.ug.R

class HomePresenter(private val context: Context, val view: HomeContract.View): HomeContract.Presenter {

    private var checkedId: Int = 0

    override fun applyResit() {
        view.showApplyResit()
    }

    override fun requestTranscript() {
        TODO("not implemented")
    }

    override fun next() {
        when (checkedId) {
            R.id.resit -> applyResit()
            R.id.transcript -> requestTranscript()
            0 -> Toast.makeText(context, "Please select one of the options above", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun itemChecked() {
        view.showNextTutorial()
    }

    val onCheckChangedListener: RadioGroup.OnCheckedChangeListener
        = RadioGroup.OnCheckedChangeListener { _, checkedId ->  this.checkedId = checkedId}
}