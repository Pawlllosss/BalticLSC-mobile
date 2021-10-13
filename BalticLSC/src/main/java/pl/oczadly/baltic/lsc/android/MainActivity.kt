package pl.oczadly.baltic.lsc.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.oczadly.baltic.lsc.Greeting
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.app.AppApi
import kotlin.coroutines.CoroutineContext
import com.bumptech.glide.Glide


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val appApi = AppApi()
        launch(Dispatchers.Main) {
            try {
                // TODO: Only the original thread that created a view hierarchy can touch its views.
                val result = withContext(Dispatchers.IO) {
                    val tv: TextView = findViewById(R.id.text_view)
                    val app = appApi.fetchApplications().data[0]
                    tv.text = app.unit.name

                    Glide.with(applicationContext)
                        .load("https://www.balticlsc.eu/model/_icons/fcr_001.png")
                        .into(findViewById(R.id.image))

                }
                Toast.makeText(this@MainActivity, result.toString(), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
