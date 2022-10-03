package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
data class Tweet(
    var id: String,
    var body: String,
    var createdAt: String,
    var user: User?
) : Parcelable {

    companion object {

        private fun fromJSON(jsonObject: JSONObject): Tweet {

            return Tweet(
                jsonObject.getString("id"),
                jsonObject.getString("text"),
                jsonObject.getString("created_at"),
                User.fromJSON(jsonObject.getJSONObject("user"))
            )
        }

        fun fromJSONArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()

            for (i in 0 until jsonArray.length()) {
                tweets.add((fromJSON(jsonArray.getJSONObject(i))))
            }

            return tweets
        }
    }
}