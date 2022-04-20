package pl.oczadly.baltic.lsc.android.view.diagram.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.diagram.service.DiagramService
import pl.oczadly.baltic.lsc.diagram.DiagramApi

class CalDiagramView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val diagramService = DiagramService(
        DiagramApi(MainActivity.apiConfig, MainActivity.state)
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_cal_diagram)
        setSupportActionBar(findViewById(R.id.toolbar))

        launch(Dispatchers.Main) {
            diagramService.getDiagramByReleaseUid("2e03c7a2-e54b-4141-b109-39f746d3b4ef")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}