package com.integratorsb2b.ug


interface BaseContract {

    interface View<in T>   {
        fun setPresenter(presenter: T)
    }

    interface Presenter {
        fun begin()
        fun next()
    }
}