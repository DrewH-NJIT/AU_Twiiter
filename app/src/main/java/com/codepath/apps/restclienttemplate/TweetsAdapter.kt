package com.codepath.apps.restclienttemplate

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TWEET_EXTRA = "TWEET_EXTRA"

class TweetsAdapter(
    private val context: Context,
    val tweets: ArrayList<Tweet>
) :
    RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_tweet, parent, false)

        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        val tweet: Tweet = tweets[position]

        holder.bind(tweet)
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

    override fun getItemCount() = tweets.size


    fun getOldestTweet(): Tweet {
        return tweets[tweets.size - 1]
    }

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener {
        private val ivProfileImage: ImageView =
            itemView.findViewById<ImageView>(R.id.ivProfileImage)
        private val tvUserName: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        private val tvTweetBody: TextView = itemView.findViewById<TextView>(R.id.tvTweetBody)
        private val tvTime: TextView = itemView.findViewById<TextView>(R.id.tvTime)

        init {
            itemView.setOnLongClickListener(this)
        }

        fun bind(tweet: Tweet) {

            val timeStringMin: String = getTimeInMs(tweet)

            tvUserName.text = tweet.user?.name
            tvTweetBody.text = tweet.body

            tvTime.text = "$timeStringMin ago"

            Glide.with(itemView).load(tweet.user?.publicImageURL).into(ivProfileImage)
        }

        override fun onLongClick(view: View): Boolean {
            val tweet = tweets[adapterPosition]

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(TWEET_EXTRA, tweet)
            context.startActivity(intent)

            return true
        }

    }

}