package com.example.coroutines_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var tvAdvice : TextView
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAdvice = findViewById(R.id.tvMain)
        button = findViewById(R.id.button)

        button.setOnClickListener { requestAPI() }
    }

    private fun requestAPI(){
        CoroutineScope(IO).launch {
            Log.d("MAIN", "fetch advice")
            val advice = async { fetchAdvice() }.await()
            if(advice.isNotEmpty()){ updateTextView(advice) }
            else{ Log.d("MAIN", "Unable to get data") }
        }
    }


    private fun fetchAdvice(): String{
        Log.d("MAIN", "went inside fetch")
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<Advice> = apiInterface!!.getAdvice()
        var advice = ""

        try {
            val response = call.execute()
            advice = response.body()?.slip?.advice.toString()
            Log.d("MAIN", "read advice")
        }
        catch (e: Exception){Log.d("MAIN", "ISSUE: $e")}


        Log.d("MAIN", "advice is $advice")
        return advice
    }

    private suspend fun updateTextView(advice : String) {
        withContext(Main){
            tvAdvice.text = advice
        }
    }
}

/*
1- fetch data from  https://api.adviceslip.com/advice
2- the data fetching should be done in the background using coroutines when a button is clicked
3- display the advice in a text box

- conncect api
- connect coroutines
- layout button and tv
* */