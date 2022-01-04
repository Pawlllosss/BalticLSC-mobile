package pl.oczadly.baltic.lsc.android.view.app.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi

class AppDeleteView : AppCompatActivity(), CoroutineScope {

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
        val appListItem =
            intent.getSerializableExtra(AppStoreView.appListItemIntent) as? AppListItemEntity
        setContentView(R.layout.activity_delete_resource)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (appListItem == null) {
            finish()
        } else {

            findViewById<TextView>(R.id.resource_delete_message_text_view).text =
                "Are you sure you want to delete the app?"
            findViewById<TextView>(R.id.resource_delete_name_text_view).text = appListItem.name
            findViewById<Button>(R.id.resource_delete_delete_button)
                .setOnClickListener {
                    launch(job) {
                        appService.deleteApp(appListItem.uid)
                    }
                    finish()
                }

            findViewById<Button>(R.id.resource_delete_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }
}
