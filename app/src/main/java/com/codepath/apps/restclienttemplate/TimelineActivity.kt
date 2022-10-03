package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient

    lateinit var rvTweets: RecyclerView

    lateinit var adapter: TweetsAdapter

    lateinit var swipeContainer : SwipeRefreshLayout

    var tweetsList = ArrayList<Tweet>()

    lateinit var endListener : EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)

        swipeContainer= findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            Log.i(TAG, "onCreate: yyooooooooo")
            populateHomeTimeline()
        }

        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light,
        )

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(this, tweetsList)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        endListener = object : EndlessRecyclerViewScrollListener(rvTweets.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreTweets(tweetsList[tweetsList.size-1].id.toLong())
            }
        }

        rvTweets.addOnScrollListener(endListener);

        populateHomeTimeline()
    }

    fun loadMoreTweets(startID: Long) {
        // 1. Send an API request to retrieve appropriate paginated data
        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
        Log.i(TAG, "loadMoreData: hi")
        client.loadMoreTweets(startID, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess:")

                val jsonArray = json.jsonArray
                try {
                    val listOfNewTweetsRetrieved = Tweet.fromJSONArray(jsonArray)
                    tweetsList.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()

                    swipeContainer.isRefreshing = false
                } catch (e: JSONException) {
                    Log.e(TAG, "json exception: $e ")
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: $response")
            }


        })
    }

    private fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess:")

                val jsonArray = json.jsonArray
                try {
                    adapter.clear()
                    val listOfNewTweetsRetrieved = Tweet.fromJSONArray(jsonArray)
                    tweetsList.addAll(listOfNewTweetsRetrieved)
                    adapter.notifyDataSetChanged()

                    swipeContainer.isRefreshing = false
                } catch (e: JSONException) {
                    Log.e(TAG, "json exception: $e ")
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: $response")
            }


        })
    }

    companion object {
        val TAG = "TimelineActivity"
    }
}