package com.integratorsb2b.ug.resit

import android.content.Context
import android.databinding.ObservableField
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.integratorsb2b.ug.Payload
import com.integratorsb2b.ug.Util

class ResitPresenter(private val context: Context,
                     private val view: ResitContract.View) : ResitContract.Presenter {

    var unitCost: ObservableField<String> = ObservableField()
    var creditHours: ObservableField<String> = ObservableField("1")
    var studentNumber: ObservableField<String> = ObservableField()

    private lateinit var selectedLevel: String
    private lateinit var selectedProgramme: String
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

        val programmes: ArrayList<String> = ArrayList()
        chargesMap[level]?.mapTo(programmes) { it.facultyName }

        return programmes
    }

    override fun setProgramme(programme: String) {
        selectedProgramme = programme
        unitCost.set("Charge per credit hour is GHS " + getCharge())
    }

    private fun getCharge(): Double {
        val chargeList: ArrayList<Charge>? = chargesMap[selectedLevel]

        if (chargeList != null) {
            for (c in chargeList) {
                if (c.facultyName == selectedProgramme) {
                    return c.creditCharges[0].price
                }
            }
        }

        return 0.0
    }

    override fun next() {
        val payload = Payload("resit")

        if (!Util.isValidStudentNumber(studentNumber)) {
            view.showNoStudentNumberError()
            return
        }

        if (creditHours.get() == null || creditHours.get().isEmpty() || creditHours.get().toInt() < 1) {
            view.showInvalidCreditHoursError()
            return
        }

        payload.form.put("creditHours", creditHours.get().toInt())
        payload.form.put("faculty", selectedProgramme)
        payload.form.put("category", selectedLevel)
        payload.form.put("studentNumber", studentNumber.get())
        payload.form.put("charge", getCharge())
        view.showConfirmation(payload)
    }

    override fun begin() {
//        view.setProgrammeOptions(arrayOf("Humanities", "Sciences", "Administration", "Agriculture"))
//        view.setLevelOptions(arrayOf("Undergraduate", "Post Graduate", "Foreign"))
        fetchConfigs()
    }

    fun fetchConfigs() {
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