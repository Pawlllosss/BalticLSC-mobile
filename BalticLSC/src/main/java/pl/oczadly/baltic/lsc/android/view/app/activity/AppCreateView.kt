package pl.oczadly.baltic.lsc.android.view.app.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi

class AppCreateView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_create)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<Button>(R.id.app_create_create_button)
            .setOnClickListener {
                val appName =
                    findViewById<EditText>(R.id.app_create_name_edit_text).text.toString().trim()
                launch(job) {
                    appService.createApp(appName)
                }
                setResult(Activity.RESULT_OK)
                finish()
            }

        findViewById<Button>(R.id.app_create_cancel_button)
            .setOnClickListener {
                finish()
            }
    }
}
