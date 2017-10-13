package com.integratorsb2b.ug

import java.io.Serializable


class Payload(val type: String, val form: HashMap<String, Any> = HashMap()) : Serializable {

    public class PaymentOptions {
        companion object {
            val mtnMomo = "MTN Mobile Money"
            val airtelMoney = "Airtel Money"
            val visa = "visa"
            val masterCard = "mastercard"
            val tigoCash = "Tigo Cash"

        }
    }
}