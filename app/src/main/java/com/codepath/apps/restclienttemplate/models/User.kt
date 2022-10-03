package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    var name: String,
    var screenName: String,
    var publicImageURL: String

) : Parcelable {

    companion object {
        fun fromJSON(jsonObject: JSONObject): User {

            return User(
                jsonObject.getString("name"),
                jsonObject.getString("screen_name"),
                jsonObject.getString("profile_image_url_https")
            )
        }
    }
}