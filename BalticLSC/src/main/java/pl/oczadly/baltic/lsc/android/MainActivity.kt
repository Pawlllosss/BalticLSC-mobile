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

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()

        val appApi = AppApi()
        launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) { appApi.fetchApplications().data[0] }
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
