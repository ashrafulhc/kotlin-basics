package com.example.networkcall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {
    private lateinit var headingTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headingTV = findViewById(R.id.heading_tv)

        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                val responseBody = response.body()!!
                val myStringBuilder = StringBuilder()

                for (myData in responseBody) {
                    myStringBuilder.append(myData.title)
                    myStringBuilder.append("\n")
                }

                headingTV.text = myStringBuilder
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}")
            }
        })

        // post related code
        val myDataItem = MyDataItem("I am trying to make a post request", 101, "Post Request", 22)
        val retrofitAddPost = retrofitBuilder.addPost(myDataItem)

        retrofitAddPost.enqueue(object : Callback<MyDataItem?> {
            override fun onResponse(call: Call<MyDataItem?>, response: Response<MyDataItem?>) {
                val responseBody = response.body()!!
                Log.d("MainActivity", "Here is the title: ${responseBody.title}")
            }

            override fun onFailure(call: Call<MyDataItem?>, t: Throwable) {
                Log.e("MainActivity", t.toString())
            }
        })
    }
}