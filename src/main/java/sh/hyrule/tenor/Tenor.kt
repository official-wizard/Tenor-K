package sh.hyrule.tenor

import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException

class Tenor(private val apiKey: String) {

    fun random(search: String): JSONArray? {
        val results = search(search).optJSONArray("results")
        val result = results.optJSONObject((0..results.length()).random())

        return result?.getJSONArray("media")
    }

    fun search(search: String): JSONObject {
        return get(String.format(
            "https://g.tenor.com/v1/search?q=%1\$s&key=%2\$s&limit=%3\$s",
            search, apiKey, 10
        ))
    }

    private operator fun get(url: String): JSONObject {
        val connection = Jsoup.connect(url)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .ignoreHttpErrors(true).header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json; charset=UTF-8")

        val response = connection.execute()
        val statusCode = response.statusCode()

        if (statusCode != 200 && statusCode != 201) {
            throw IOException(String.format("HTTP Code: '%s' from '%s'", statusCode, url))
        }

        return JSONObject(response.body())
    }
}