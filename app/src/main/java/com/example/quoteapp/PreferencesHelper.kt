import android.content.Context
import android.content.SharedPreferences
import com.example.quoteapp.Quote
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveFavoritesList(favorites: List<Quote>) {
        val jsonString = gson.toJson(favorites)
        sharedPreferences.edit().putString("favorites_list", jsonString).apply()
    }

    fun getFavoritesList(): List<Quote> {
        val jsonString = sharedPreferences.getString("favorites_list", null)
        return if (jsonString != null) {
            val type = object : TypeToken<List<Quote>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            emptyList()  // Return an empty list if no data is saved
        }
    }
}