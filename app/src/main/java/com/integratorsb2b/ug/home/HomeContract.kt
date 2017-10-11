package com.integratorsb2b.ug.home

import com.integratorsb2b.ug.BaseContract

interface HomeContract {

    interface View: BaseContract.View<Presenter> {
        fun showApplyResit()
        fun showRequestTranscript()
        fun showNextTutorial()
    }

    interface Presenter: BaseContract.Presenter {
        fun applyResit()
        fun requestTranscript()
        fun itemChecked()
    }
}