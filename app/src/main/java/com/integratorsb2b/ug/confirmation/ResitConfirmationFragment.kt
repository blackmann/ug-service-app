package com.integratorsb2b.ug.confirmation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R


class ResitConfirmationFragment : Fragment() {

    companion object {
        val payloadExtra = "ug_payload_extra"

        fun getInstance(payload: Payload): ResitConfirmationFragment {
            val fragment = ResitConfirmationFragment()
            val bundle = Bundle()
            bundle.putSerializable(payloadExtra, payload)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_resit_confirm, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val payload = arguments[payloadExtra] as Payload

        view?.findViewById<TextView>(R.id.faculty)?.setText(payload.form["facultyName"] as String)
        view?.findViewById<TextView>(R.id.level)?.setText(payload.form["category"] as String)

        view?.findViewById<TextView>(R.id.credit_hours)
                ?.setText(String.format("%d (at GHS %.2f per hour)",
                        payload.form["creditHours"],
                        payload.form["charge"]))
    }
}