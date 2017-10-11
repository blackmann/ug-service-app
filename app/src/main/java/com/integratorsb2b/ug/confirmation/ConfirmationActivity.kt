package com.integratorsb2b.ug.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R


class ConfirmationActivity : AppCompatActivity() {


    companion object {
        val payloadExtra = "ug_payload_extra"

        fun start(context: Context, payload: Payload? = null) {
            val intent = Intent(context, ConfirmationActivity::class.java)
            intent.putExtra(payloadExtra, payload)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val payload: Payload? = intent.getSerializableExtra(payloadExtra) as Payload?
        if (payload != null) {
            var fragment: Fragment? = null
            if (payload.type == "transcript") fragment =
                    TranscriptConfirmationFragment.getInstance(payload)
            else if (payload.type == "resit") fragment =
                    ResitConfirmationFragment.getInstance(payload)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}