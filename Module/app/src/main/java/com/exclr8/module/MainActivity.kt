package com.exclr8.module

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.exclr8.exclr8module.api.ApiHelper
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.exclr8.exclr8module.R.layout.activity_main)

        val postObj = CallObj("Fake Data")
        val apiCall = ApiHelper().apiCall(
            postObj = postObj.toRequestBody(),
            url = "https://jsonplaceholder.typicode.com/posts",
        )
        Log.i("Main", apiCall.toString())
    }
}

@Serializable
data class CallObj(
    val string: String
)

fun CallObj.toRequestBody(): RequestBody {
    val jsonObj = Gson().toJson(this)
    return RequestBody.create("application/json".toMediaTypeOrNull(), jsonObj)
}