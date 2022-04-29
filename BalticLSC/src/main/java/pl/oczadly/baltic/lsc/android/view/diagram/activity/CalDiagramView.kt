package pl.oczadly.baltic.lsc.android.view.diagram.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.diagram.converter.DiagramEntityConverter
import pl.oczadly.baltic.lsc.android.view.diagram.converter.DrawableElementConverter
import pl.oczadly.baltic.lsc.android.view.diagram.converter.DrawableLineConverter
import pl.oczadly.baltic.lsc.android.view.diagram.converter.DrawableShapeConverter
import pl.oczadly.baltic.lsc.android.view.diagram.service.DiagramService
import pl.oczadly.baltic.lsc.diagram.DiagramApi
import kotlin.coroutines.CoroutineContext

class CalDiagramView : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val diagramService = DiagramService(
        DiagramApi(MainActivity.apiConfig, MainActivity.state),
        DiagramEntityConverter(
            DrawableElementConverter(DrawableShapeConverter()),
            DrawableLineConverter()
        )
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_cal_diagram)
        setSupportActionBar(findViewById(R.id.toolbar))
        val appRelease =
            intent.getSerializableExtra(AppStoreView.appReleaseIntent) as? AppReleaseEntity
        val calDiagramView = findViewById<CalDiagramCanvasView>(R.id.cal_diagram_view)

        if (appRelease == null) {
            finish()
        } else {
            launch(Dispatchers.Main) {
                val diagramUid = appRelease.diagramUid
                diagramUid?.let {
                    val diagramEntity = diagramService.getDiagramByDiagramUid(diagramUid)
                    calDiagramView?.let {
                        it.diagram = diagramEntity
                        it.invalidate()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
