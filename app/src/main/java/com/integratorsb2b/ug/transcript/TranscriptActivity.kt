package com.integratorsb2b.ug.transcript

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R
import com.integratorsb2b.ug.databinding.ActivityTranscriptBinding
import com.integratorsb2b.ug.payment.PaymentActivity
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt


class TranscriptActivity : AppCompatActivity(), TranscriptContract.View {
    override fun showFormError() {
        Toast.makeText(this,
                "Please provide a valid Student Number and choose a delivery choice.",
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

    override fun showPaymentForm(payload: Payload?) {
        PaymentActivity.start(this, payload)
    }

    private lateinit var localPresenter: TranscriptContract.Presenter

    override fun setPresenter(presenter: TranscriptContract.Presenter) {
        localPresenter = presenter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the presenter
        TranscriptPresenter(this, this)

        val binding: ActivityTranscriptBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_transcript)

        binding.presenter = localPresenter as TranscriptPresenter
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}