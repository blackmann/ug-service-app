package com.integratorsb2b.ug.transcript

import android.content.Context
import android.databinding.ObservableField
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.Util


class TranscriptPresenter(private val context: Context,
                          private val view: TranscriptContract.View) : TranscriptContract.Presenter {

    private lateinit var deliveryType: String
    var studentNumber: ObservableField<String> = ObservableField()
    var postalAddress: ObservableField<String> = ObservableField()

    init {
        view.setPresenter(this)
    }

    override fun begin() {
        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET,
                "https://ugapp-integratorsb2b.herokuapp.com/api/ug/transcript/type",
                { response -> handleResponse(response) },
                { error -> handleError(error) })

        view.showWait()
        requestQueue.add(request)
    }

    fun fetchConfigs() {
        val requestQueue = Volley.newRequestQueue(context)
        val request = StringRequest(Request.Method.GET,
                "https://ugapp-integratorsb2b.herokuapp.com/api/ug/transcript/type",
                { response -> handleResponse(response) },
                { error -> handleError(error) })

        view.showWait()
        requestQueue.add(request)
    }

    private fun handleError(error: VolleyError) {
        view.showConnectionError()
    }

    private lateinit var deliveryChoices: ArrayList<DeliveryChoice>

    private fun handleResponse(response: String) {
        view.hideWait()

        deliveryChoices = Gson().fromJson(response)
        val pLocations = ArrayList<String>(deliveryChoices.size)
        deliveryChoices.mapTo(pLocations, { it.name })

        view.setDeliveryChoices(pLocations)
    }

    private lateinit var location: String

    override fun setSelectedLocation(location: String) {
        this.location = location
    }

    override fun setSelectedDeliveryChoice(choice: String) {
        val locations: ArrayList<String> = ArrayList()
        deliveryType = choice
        for (d in deliveryChoices) {
            if (d.name == choice) {
                d.location.mapTo(locations, { it.key })
                view.setPostalOptions(locations)

                if (d.name.toLowerCase().contains("post")) {
                    view.showPostalForm()
                } else {
                    view.hidePostalForm()
                }

                return
            }
        }
    }

    override fun next() {
        val payload = Payload("transcript")

        if (!Util.isValidStudentNumber(studentNumber)) {
            view.showFormError()
            return
        }

        payload.form.put("indexNumber", studentNumber.get())

        if (deliveryType.toLowerCase().contains("post")) {
            if (!Util.isValidPostalAddress(postalAddress)) {
                view.showFormError()
                return
            }

            payload.form.put("address", postalAddress.get())
        }

        payload.form.put("deliveryType", deliveryType)
        payload.form.put("location", location)

        // now add the charges
        for (d in deliveryChoices) {
            if (d.name == deliveryType) {
                for (l in d.location) {
                    if (l.key == location) {
                        payload.form.put("actualCharge", l.actualCharge)
                        payload.form.put("serviceCharge", l.serviceCharge)
                        payload.form.put("postalCharge", l.postalCharge)
                    }
                }
            }
        }

        // check for the delivery
        view.showConfirmation(payload)
    }

    class Location(val key: String, val actualCharge: Double, val serviceCharge: Double,
                   val postalCharge: Double)

    class DeliveryChoice(val name: String, val location: ArrayList<Location>)
}