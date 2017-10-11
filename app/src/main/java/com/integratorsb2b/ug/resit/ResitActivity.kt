package com.integratorsb2b.ug.resit

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityResitBinding
import com.integratorsb2b.ug.payment.PaymentActivity
import com.jaredrummler.materialspinner.MaterialSpinner

class ResitActivity : AppCompatActivity(), ResitContract.View {

    lateinit var localPresenter: ResitContract.Presenter


    override fun showPaymentForm(payload: Payload?) {
        PaymentActivity.start(this, payload)
    }

    override fun setPresenter(presenter: ResitContract.Presenter) {
        localPresenter = presenter
    }

    override fun showFetchLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                ?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                ?.visibility = View.GONE
    }

    override fun setLevelOptions(options: Array<String>) {
        val levelOptionsView: MaterialSpinner =
                findViewById(R.id.levels)

        levelOptionsView.setItems(options.asList())
        levelOptionsView.setOnItemSelectedListener { _, _, _, item ->
            localPresenter.setLevel(item as String)
        }
    }

    override fun setProgrammeOptions(programmes: Array<String>) {
        val programmeOptionsView: MaterialSpinner =
                findViewById(R.id.programmes)

        programmeOptionsView.setItems(programmes.asList())
        programmeOptionsView.setOnItemSelectedListener { _, _, _, item ->
            localPresenter.setProgramme(item as String) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPresenter(ResitPresenter(this, this))

        val binding: ActivityResitBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_resit)
        binding.presenter = localPresenter as ResitPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // let's begin the drama
        localPresenter.begin()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}