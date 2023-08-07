package com.example.lbprate

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.lbprate.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//API url
const val BASE_URL = "https://rate.onrender.com/"

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        // Launch a coroutine to fetch the data
        GlobalScope.launch {
            asyncFetchLiraData()
        }


        installSplashScreen().apply {

            this.setKeepOnScreenCondition() { binding.buyRate.text == "Buy rate:" }

        }



        setContentView(binding.root)



        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchLiraData()

            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun fetchLiraData(){

        //create the retrofit builder object
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) //assigns Gson as the converter JSON <-> String
            .baseUrl(BASE_URL) //assigns the base url of the api
            .build()
            .create(ApiInterface::class.java)

        //get the rates and store it in the retrofitData variable
        val retrofitData = retrofitBuilder.getRates()

        retrofitData.enqueue(object : Callback<BSRate>{
            override fun onResponse(call: Call<BSRate>, response: Response<BSRate>) {
                val responseBody = response.body()!!
                binding.buyRate.text = "Buy rate: " + responseBody.buy_rate + " LBP"
                binding.sellRate.text = "Sell rate: " + responseBody.sell_rate + " LBP"

                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                binding.lastUpdated.text = "Last updated: " + currentTime

                binding.buyDesc.text = "Rates are according to the black market, which show the conversion for every 1 USD"
            }

            override fun onFailure(call: Call<BSRate>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
                binding.buyRate.text = ""
                binding.lastUpdated.text = "Fetching Lira rates failed"
                binding.sellRate.text = "Please connect to the internet and try again"
                binding.buyDesc.text = ""
            }
        })

    }

    private fun getLira():Array<String>{
        //create the retrofit builder object
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) //assigns Gson as the converter JSON <-> String
            .baseUrl(BASE_URL) //assigns the base url of the api
            .build()
            .create(ApiInterface::class.java)

        //get the rates and store it in the retrofitData variable
        val retrofitData = retrofitBuilder.getRates()

        var rateArray = emptyArray<String>()
        retrofitData.enqueue(object : Callback<BSRate>{
            override fun onResponse(call: Call<BSRate>, response: Response<BSRate>) {
                val responseBody = response.body()!!
                rateArray[0] = responseBody.buy_rate
                rateArray[1] = responseBody.sell_rate
            }

            override fun onFailure(call: Call<BSRate>, t: Throwable) {
                Log.d("MainActivity", "onFailure: " + t.message)
                binding.buyRate.text = ""
                binding.lastUpdated.text = "Fetching Lira rates failed"
                binding.sellRate.text = "Please connect to the internet and try again"
                binding.buyDesc.text = ""
            }
        })
        return rateArray
    }

    private suspend fun asyncFetchLiraData() {
        try {
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.getRates()

            // Update UI on the main thread
            withContext(Dispatchers.Main) {
                retrofitData.enqueue(object : Callback<BSRate>{
                    override fun onResponse(call: Call<BSRate>, response: Response<BSRate>) {
                        val responseBody = response.body()!!
                        binding.buyRate.text = "Buy rate: " + responseBody.buy_rate + " LBP"
                        binding.sellRate.text = "Sell rate: " + responseBody.sell_rate + " LBP"

                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        binding.lastUpdated.text = "Last updated: " + currentTime

                        binding.buyDesc.text = "Rates are according to the black market, which show the conversion for every 1 USD"
                    }

                    override fun onFailure(call: Call<BSRate>, t: Throwable) {
                        Log.d("MainActivity", "onFailure: " + t.message)
                        binding.buyRate.text = ""
                        binding.lastUpdated.text = "Fetching Lira rates failed"
                        binding.sellRate.text = "Please connect to the internet and try again"
                        binding.buyDesc.text = ""
                    }
                })
            }
        } catch (e: Exception) {
            Log.d("MainActivity", "onFailure: ${e.message}")
            withContext(Dispatchers.Main) {
                binding.buyRate.text = ""
                binding.lastUpdated.text = "Fetching Lira rates failed"
                binding.sellRate.text = "Please connect to the internet and try again"
                binding.buyDesc.text = ""
            }
        }
    }

}




