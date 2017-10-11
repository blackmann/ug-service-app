package com.integratorsb2b.ug

import java.io.Serializable


class Payload(val type: String, val form: HashMap<String, Any> = HashMap()) : Serializable