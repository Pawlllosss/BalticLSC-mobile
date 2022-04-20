package pl.oczadly.baltic.lsc.android.view.cal.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppResourceRangeEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi

class CalDiagramView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.apiConfig, MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    private val resourceRangeConverter = AppResourceRangeEntityConverter()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_cal_diagram)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
