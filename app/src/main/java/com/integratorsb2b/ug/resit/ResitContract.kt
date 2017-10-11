package com.integratorsb2b.ug.resit

import com.integratorsb2b.ug.BaseContract

interface ResitContract {

    interface Presenter {
        fun next()

    }

    interface View: BaseContract.View<Presenter> {
        fun showFetchLoading()
    }
}