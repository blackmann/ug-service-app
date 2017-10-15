package com.integratorsb2b.ug.transcript

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.confirmation.ConfirmationActivity
import com.integratorsb2b.ug.databinding.ActivityTranscriptBinding
import com.integratorsb2b.ug.payment.PaymentActivity
import com.jaredrummler.materialspinner.MaterialSpinner
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt


class TranscriptActivity : AppCompatActivity(), TranscriptContract.View {

    // also known as the locations
    override fun setPostalOptions(options: ArrayList<String>) {
        val materialSpinner = findViewById<MaterialSpinner>(R.id.location)
        // hack, material spinner doesn't seem to behave when the list is of size 1
        if (options.size == 1) {
            materialSpinner.setItems(options[0], options[0])
        } else {
            materialSpinner.setItems(options)
        }

        materialSpinner.setOnItemSelectedListener { _, _, _, item ->
            presenter.setSelectedLocation(item as String)
        }

        presenter.setSelectedLocation(options[0])
    }

    // also known as the delivery choice
    override fun setDeliveryChoices(locations: ArrayList<String>) {
        val materialSpinner = findViewById<MaterialSpinner>(R.id.delivery_choice)
        materialSpinner.setItems(locations)

        materialSpinner.setOnItemSelectedListener({ _, _, _, item -> presenter.setSelectedDeliveryChoice(item as String) })

        // select the first one as default
        presenter.setSelectedDeliveryChoice(locations[0])
    }

    override fun showWait() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.VISIBLE

        findViewById<TextView>(R.id.info)
                .setText(R.string.setting_up)

        findViewById<ProgressBar>(R.id.progress)
                .visibility = View.VISIBLE

        findViewById<Button>(R.id.retry)
                .isEnabled = false
    }

    override fun hideWait() {
        findViewById<LinearLayout>(R.id.waiting)
                .visibility = View.GONE
    }

    override fun showConnectionError() {
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

    override fun showFormError() {
        Toast.makeText(this,
                "Please provide a valid Student Number and postal address if required.",
                Toast.LENGTH_SHORT).show()
    }

    override fun showPostalAddressNotSpecifiedError() {
        Toast.makeText(this,
                "Please enter a full postal address.", Toast.LENGTH_SHORT)
                .show()
    }

    override fun hidePostalForm() {
        findViewById<LinearLayout>(R.id.postalForm)
                .visibility = View.GONE
    }

    override fun showPostalForm() {
        findViewById<LinearLayout>(R.id.postalForm)
                .visibility = View.VISIBLE
    }

    override fun showNextTap() {
        val tapTarget: MaterialTapTargetPrompt = MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.next)
                .setPrimaryText("Tap here to continue to Payment")
                .create()

        Handler().postDelayed({ tapTarget.show() }, 900)
        Handler().postDelayed({ tapTarget.finish() }, 2500)
    }

    override fun showPayment(payload: Payload?) {
        PaymentActivity.start(this, payload)
    }

    private lateinit var presenter: TranscriptContract.Presenter

    override fun setPresenter(presenter: TranscriptContract.Presenter) {
        this.presenter = presenter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the presenter
        TranscriptPresenter(this, this)

        val binding: ActivityTranscriptBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_transcript)

        binding.presenter = presenter as TranscriptPresenter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.begin()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}