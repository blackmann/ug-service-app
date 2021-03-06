package com.integratorsb2b.ug.resit

import com.integratorsb2b.ug.BaseContract
import com.integratorsb2b.ug.Payload

interface ResitContract {

    interface Presenter {
        fun next()
        fun setLevel(level: String)
        fun setProgramme(programme: String)
        fun begin()
    }

    interface View: BaseContract.View<Presenter> {
        fun showFetchLoading()
        fun hideLoading()
        fun setLevelOptions(options: List<String>)
        fun setProgrammeOptions(programmes: List<String>)
        fun showConfirmation(payload: Payload? = null)
        fun showNoConnectionError()
        fun hideRetryControl()
        fun showNoStudentNumberError()
        fun showInvalidCreditHoursError()
        fun showInvalidMobileNumber()
    }
}