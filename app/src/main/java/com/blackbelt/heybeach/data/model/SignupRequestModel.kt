package com.blackbelt.heybeach.data.model

import org.json.JSONObject

data class SignUpRequestModel(val email: String, val password: String) {

    fun toJsonString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)
        return jsonObject.toString()
    }
}