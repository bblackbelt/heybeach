package com.blackbelt.heybeach.data

import com.blackbelt.heybeach.data.model.BeachResponse
import com.blackbelt.heybeach.data.model.ErrorResponse
import com.blackbelt.heybeach.data.model.SignUpResponseModel
import com.blackbelt.heybeach.data.model.UserResponseModel
import org.json.JSONArray
import org.json.JSONObject

object ResponseParser {

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

    fun toSignupResonseModel(jsonString: String): SignUpResponseModel {
        try {
            val obj = JSONObject(jsonString)
            val email = obj.optString("email")
            val id = obj.optString("_id")
            return SignUpResponseModel(id, email)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return SignUpResponseModel()
    }

    fun toUserResponseMode(jsonString: String?): UserResponseModel {
        try {
            val obj = JSONObject(jsonString)
            val email = obj.optString("email")
            val id = obj.optString("_id")
            return UserResponseModel(id, email)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return UserResponseModel()
    }

    fun toErrorResponseModel(jsonString: String?): ErrorResponse {
        try {
            val obj = JSONObject(jsonString)
            val code = obj.optInt("code")
            val index = obj.optInt("index")
            val errorMessage = obj.optString("errmsg")
            return ErrorResponse(code, index, errorMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ErrorResponse()
    }
}