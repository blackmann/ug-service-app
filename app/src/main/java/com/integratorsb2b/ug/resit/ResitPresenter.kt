package com.integratorsb2b.ug.resit

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.integratorsb2b.ug.Payload

class ResitPresenter(private val context: Context,
                     private val view: ResitContract.View) : ResitContract.Presenter {
    lateinit var selectedLevel: String
    lateinit var selectedProgramme: String
    private var retries = 0

    private val chargesRequestTag = "charges_t_"

    private lateinit var categories: ArrayList<Category>
    private var chargesMap: HashMap<String, ArrayList<Charge>> = HashMap()

    init {
        view.setPresenter(this)
    }

    override fun setLevel(level: String) {
        selectedLevel = level
        view.setProgrammeOptions(getProgrammeOptions(level))
    }

    private fun getProgrammeOptions(level: String): List<String> {
        val chargesList = chargesMap[level]

        val programmes: ArrayList<String> = ArrayList()
        if (chargesList != null) {
            for (c in chargesList) {
                programmes.add(c.facultyName)
            }
        }

        return programmes
    }

    override fun setProgramme(programme: String) {
        selectedProgramme = programme
    }

    override fun next() {
        val payload = Payload("resit")
        view.showPaymentForm(payload)
    }

    override fun begin() {
//        view.setProgrammeOptions(arrayOf("Humanities", "Sciences", "Administration", "Agriculture"))
//        view.setLevelOptions(arrayOf("Undergraduate", "Post Graduate", "Foreign"))
        fetchConfigs()
    }

    private fun fetchConfigs() {
        view.hideRetryControl()
        view.showFetchLoading()
        val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)

        val categoryRequest = StringRequest(Request.Method.GET,
                "http://ugapp-integratorsb2b.herokuapp.com/api/ug/resit/category",
                { response -> handleResponse(response) },
                { error -> handleError(error) })

        requestQueue.add(categoryRequest)
    }

    private fun handleError(error: VolleyError) {
        if (error is TimeoutError) {
            if (retries++ < 2) {
                fetchConfigs()
            }
        } else if (error is NoConnectionError) {
            view.showNoConnectionError()
        }
    }

    private fun handleResponse(jsonResponse: String) {
        categories = Gson().fromJson(jsonResponse)

        val requestQueue: RequestQueue = Volley.newRequestQueue(context.applicationContext)
        // fetch all prices for each category
        for (c: Category in categories) {
            val request = StringRequest(Request.Method.GET,
                    "http://ugapp-integratorsb2b.herokuapp.com/api/ug/resit/faculty/charges/" + c._id,
                    { response -> handleChargesResponse(response, c) },
                    { error -> handleChargesError(requestQueue) })

            request.tag = chargesRequestTag
            requestQueue.add(request)
        }
    }

    private fun handleChargesError(requestQueue: RequestQueue) {
        requestQueue.cancelAll(chargesRequestTag)
        view.showNoConnectionError()
    }

    private fun handleChargesResponse(response: String, category: Category) {
        val chargesList: ArrayList<Charge> = Gson().fromJson(response)
        chargesMap.put(category.name, chargesList)

        // verify if all is set then proceed
        if (chargesMap.keys.size == categories.size) {
            proceed()
        }
    }

    private fun proceed() {
        view.hideLoading()
        view.setLevelOptions(chargesMap.keys.toList())
    }

    class Category(val _id: String, val name: String)

    class CreditCharge(val name: String, val price: Double)

    class Charge(val facultyName: String,
                 val category: String,
                 val creditCharges: ArrayList<CreditCharge>)
}