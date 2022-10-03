package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import java.text.SimpleDateFormat

class DetailsActivity : AppCompatActivity() {

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvTweetBody: TextView
    private lateinit var tvTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvUserName = findViewById(R.id.tvUsername)
        tvTweetBody = findViewById(R.id.tvTweetBody)
        tvTime = findViewById(R.id.tvTime)

        val tweet = intent.getParcelableExtra<Tweet>(TWEET_EXTRA) as Tweet

        val timeStringMin: String = getTimeInMs(tweet)

        Glide.with(this).load(tweet.user?.publicImageURL).into(ivProfileImage)
        tvUserName.text = tweet.user?.name
        tvTweetBody.text = tweet.body

        tvTime.text = "$timeStringMin ago"

    }

    private fun getTimeInMs(tweet: Tweet): String {

        val format: String = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val s = SimpleDateFormat(format)
        val ii: Long = s.parse(tweet.createdAt).time
        val ii2: Long = System.currentTimeMillis()
        val utct: Long = ((ii2 - ii) / 1000) + (30 * 60)

        //if under 60, print s. if under 60*24, print h.
        var timeSuffix: String = ""
        val timeMinimized: Long

        if (utct < 60) {
            timeSuffix = "s"
            timeMinimized = utct
        } else if (utct < 60 * 60) {
            timeSuffix = "m"
            timeMinimized = utct / 60
        } else if (utct < 60 * 60 * 24) {
            timeSuffix = "h"
            timeMinimized = utct / 60 / 60
        } else {
            timeSuffix = "d"
            timeMinimized = utct / 60 / 60 / 24
        }

        return "$timeMinimized$timeSuffix"
    }

}