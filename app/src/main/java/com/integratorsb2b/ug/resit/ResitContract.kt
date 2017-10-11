package com.integratorsb2b.ug.resit

import com.integratorsb2b.ug.BaseContract

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
        fun setLevelOptions(options: Array<String>)
        fun setProgrammeOptions(programmes: Array<String>)
    }
}