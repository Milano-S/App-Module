package com.exclr8.exclr8module.api

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

private const val TAG = "ApiHelper"
class ApiHelper {
    private val client = OkHttpClient()

    fun apiCall(
        postObj: RequestBody,
        url: String,
    ): Response? {

        val request = Request.Builder()
            .url(url)
            //.headers(header)
            .post(postObj)
            //.get()
            .build()

        var apiResponse: Response? = null

        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    apiResponse = response
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.i(TAG, e.message.toString())
                    return
                }

            })
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
        }
        return apiResponse
    }
}

