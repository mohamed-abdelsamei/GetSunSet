package com.c0d3r.getsunset.getsunset

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun GetSunSet(view: View){
        println("xxxxxx")
        var city = txtCityName.text.toString()
        var state = "kb"
        var url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask:AsyncTask<String,String,String>(){

        override fun onPreExecute() {
            // do before statr
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = JSONObject(values[0])
                var query = json.getJSONObject("query")
                var results = query.getJSONObject("results")
                var channel = results.getJSONObject("channel")
                var astronomy = channel.getJSONObject("astronomy")
                var sunrise = astronomy.getString("sunrise")
                txtSunSetResutl.setText("Sunset is at $sunrise")
            }catch (ex:Exception){

            }
        }
        override fun doInBackground(vararg params: String?): String {
            try{
                val url = URL(params[0])
                val urlConnecrion = url.openConnection() as HttpURLConnection
                urlConnecrion.connectTimeout = 7000

                var inSreing = ConvertStreamToString(urlConnecrion.inputStream)

                publishProgress(inSreing)
            }catch (ex:Exception){}

            return ""

        }
        override fun onPostExecute(result: String?) {
            // do when done
        }
    }

    fun  ConvertStreamToString(inputStream: InputStream): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String=""
        try {
            do {
                line = bufferReader.readLine()
                if (line != null){
                    AllString +=line
                }
            }while (line != null)
        }catch (ex:Exception){}
        return AllString
    }
}
