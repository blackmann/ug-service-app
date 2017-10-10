package com.integratorsb2b.ug.createaccount

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityCreateAccountBinding


class CreateAccountActivity: AppCompatActivity(),
        CreateAccountContract.View {
    override fun setPresenter(presenter: CreateAccountContract.Presenter) {
        TODO("not implemented")
    }

    override fun showHome() {
        TODO("not implemented")
    }

    override fun goBack() {
        TODO("not implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCreateAccountBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_create_account)
    }
}