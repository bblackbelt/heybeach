package com.blackbelt.heybeach.data

import com.blackbelt.heybeach.data.model.BeachResponse
import org.json.JSONArray

object ParsingFactory {

    fun toBeachList(beachesJsonResponse: String): List<BeachResponse> {
        val beaches = mutableListOf<BeachResponse>()
        try {
            val beachesArray = JSONArray(beachesJsonResponse)
            (0 until beachesArray.length())
                    .map { beachesArray.optJSONObject(it) }
                    .filter { it != null }
                    .map {
                        beaches.add(BeachResponse.from(it))
                    }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return beaches
    }

}