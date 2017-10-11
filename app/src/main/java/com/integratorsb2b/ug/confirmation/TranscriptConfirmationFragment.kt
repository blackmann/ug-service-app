package com.integratorsb2b.ug.confirmation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.R


class TranscriptConfirmationFragment: Fragment() {

    companion object {
        public val payloadExtra = "ug_payload_extra"

        fun getInstance(payload: Payload): TranscriptConfirmationFragment {
            val fragment = TranscriptConfirmationFragment()
            val bundle = Bundle()
            bundle.putSerializable(payloadExtra, payload)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_transcript_confirm, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}