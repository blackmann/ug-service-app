package com.integratorsb2b.ug

import java.io.Serializable


class Payload(private val type: String, private val form: HashMap<String, Any> = HashMap()) : Serializable