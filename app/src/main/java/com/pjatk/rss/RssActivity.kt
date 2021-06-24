package com.pjatk.rss

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.pjatk.rss.Adapter.FeedAdapter
import com.pjatk.rss.Common.HTTPDataHandler
import com.pjatk.rss.Model.RSSObject

import kotlinx.android.synthetic.main.activity_rss.*
import java.lang.StringBuilder

class RssActivity : AppCompatActivity() {
    private val RSS_link = "https://wiadomosci.gazeta.pl/pub/rss/wiadomosci.htm"
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss)

        //toolbar.title

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager

        loadRSS()

    }

    private fun loadRSS() {
    val loadRSSAsync = object :AsyncTask<String,String,String>(){
        internal var mDialog = ProgressDialog(this@RssActivity)
        override fun doInBackground(vararg params: String): String {
        val result:String
        val http = HTTPDataHandler()
        result = http.GetHTTPDataHandler(params[0])
            return result

        }

        override fun onPreExecute() {
            mDialog.setMessage("Please wait...")
            mDialog.show()
        }

        override fun onPostExecute(result: String?) {
        mDialog.dismiss()
            var rssObject:RSSObject
            rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java!! )
            val adapter = FeedAdapter(rssObject, baseContext)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh)
            loadRSS()

        return true
    }
}