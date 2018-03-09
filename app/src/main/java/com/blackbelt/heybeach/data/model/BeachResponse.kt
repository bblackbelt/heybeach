package com.blackbelt.heybeach.data.model

import org.json.JSONObject

data class BeachResponse(
        val id: String? = null,
        val name: String? = null,
        val url: String? = null,
        val width: String? = null,
        val height: String? = null
) {
    companion object {
        fun from(jsonObject: JSONObject): BeachResponse {
            val name = jsonObject.optString("name")
            val width = jsonObject.optString("width")
            val id = jsonObject.optString("id")
            val url = jsonObject.optString("url")
            val height = jsonObject.optString("height")

            return BeachResponse(id, name, url, width, height)
        }
    }
}

