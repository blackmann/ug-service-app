package com.integratorsb2b.ug.confirmation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R


class ConfirmationActivity: AppCompatActivity() {


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
    }
}