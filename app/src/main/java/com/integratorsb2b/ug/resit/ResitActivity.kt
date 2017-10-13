package com.integratorsb2b.ug.resit

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.confirmation.ConfirmationActivity
import com.integratorsb2b.ug.databinding.ActivityResitBinding
import com.integratorsb2b.ug.payment.PaymentActivity
import com.jaredrummler.materialspinner.MaterialSpinner

class ResitActivity : AppCompatActivity(), ResitContract.View {

    override fun showNoStudentNumberError() {
        Toast.makeText(this, "Please provide a valid student number", Toast.LENGTH_SHORT)
                .show()
    }

    override fun showInvalidCreditHoursError() {
        Toast.makeText(this, "Credit hours should be a number (greater than 0)", Toast.LENGTH_SHORT)
                .show()
    }

    override fun showNoConnectionError() {
        Toast.makeText(this, "Connection failed. Please check your network and retry.",
                Toast.LENGTH_LONG).show()

        findViewById<Button>(R.id.retry)
                .visibility = View.VISIBLE

        findViewById<TextView>(R.id.info)
                .setText(R.string.connection_failed)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.GONE

        findViewById<Button>(R.id.retry)
                .isEnabled = true
    }

    override fun hideRetryControl() {
        findViewById<Button>(R.id.retry)
                .isEnabled = false

        findViewById<TextView>(R.id.info)
                .setText(R.string.setting_up)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.VISIBLE
    }

    private lateinit var presenter: ResitContract.Presenter


    override fun showConfirmation(payload: Payload?) {
        ConfirmationActivity.start(this, payload)
    }

    override fun setPresenter(presenter: ResitContract.Presenter) {
        this.presenter = presenter
    }

    override fun showFetchLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                ?.visibility = View.VISIBLE

        findViewById<CoordinatorLayout>(R.id.main)
                ?.visibility = View.GONE
    }

    override fun hideLoading() {
        findViewById<LinearLayout>(R.id.waiting)
                ?.visibility = View.GONE

        findViewById<CoordinatorLayout>(R.id.main)
                ?.visibility = View.VISIBLE
    }

    override fun setLevelOptions(options: List<String>) {
        val levelOptionsView: MaterialSpinner =
                findViewById(R.id.levels)

        levelOptionsView.setItems(options)
        levelOptionsView.setOnItemSelectedListener { _, _, _, item ->
            presenter.setLevel(item as String)
        }

        // select the first as default
        presenter.setLevel(options[0])
    }

    override fun setProgrammeOptions(programmes: List<String>) {
        val programmeOptionsView: MaterialSpinner =
                findViewById(R.id.programmes)

        programmeOptionsView.setItems(programmes)
        programmeOptionsView.setOnItemSelectedListener { _, _, _, item ->
            presenter.setProgramme(item as String)
        }

        // select the first as default
        presenter.setProgramme(programmes[0])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the presenter
        ResitPresenter(this, this)
        val binding: ActivityResitBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_resit)
        binding.presenter = presenter as ResitPresenter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // let's begin the drama
        presenter.begin()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}