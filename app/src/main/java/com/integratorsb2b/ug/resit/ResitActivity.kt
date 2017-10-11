package com.integratorsb2b.ug.resit

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityResitBinding

class ResitActivity : AppCompatActivity(), ResitContract.View {

    lateinit var localPresenter: ResitContract.Presenter

    override fun setPresenter(presenter: ResitContract.Presenter) {
        localPresenter = presenter
    }

    override fun showFetchLoading() {
        TODO("not implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenter(ResitPresenter(this, this))

        val binding: ActivityResitBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_resit)
        binding.presenter = localPresenter as ResitPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}