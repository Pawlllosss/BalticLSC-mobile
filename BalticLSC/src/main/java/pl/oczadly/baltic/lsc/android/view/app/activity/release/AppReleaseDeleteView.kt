package pl.oczadly.baltic.lsc.android.view.app.activity.release

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
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi

class AppReleaseDeleteView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.apiBasePath, MainActivity.apiPort, MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appRelease =
            intent.getSerializableExtra(AppStoreView.appReleaseIntent) as? AppReleaseEntity
        setContentView(R.layout.activity_delete_resource)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (appRelease == null) {
            finish()
        } else {

            findViewById<TextView>(R.id.resource_delete_message_text_view).text =
                "Are you sure you want to delete the app release?"
            findViewById<TextView>(R.id.resource_delete_name_text_view).text =
                appRelease.versionName
            findViewById<Button>(R.id.resource_delete_delete_button)
                .setOnClickListener {
                    launch(job) {
                        appService.deleteRelease(appRelease.releaseUid)
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
