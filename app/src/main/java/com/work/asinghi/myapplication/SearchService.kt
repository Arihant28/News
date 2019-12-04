package com.work.asinghi.myapplication

import android.net.Uri
import android.util.Log
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import io.reactivex.Observable

object SearchService {

    var client: OkHttpClient = OkHttpClient()

    fun getSearchList(searchText: String, page: Int) = Observable.fromCallable {
        val api: String =
            "https://hn.algolia.com/api/v1/search/?query=" + Uri.encode(searchText) + "&page=" + page


        var output: String = ""
        val request = Request.Builder()
            .url(api)
            .build()
        try {
            val response: Response = client.newCall(request).execute()
            output = response.body().string()
        } catch (e: Throwable) {
            Log.e("Network request", "Failure", e)
        }

        output
    }
}
