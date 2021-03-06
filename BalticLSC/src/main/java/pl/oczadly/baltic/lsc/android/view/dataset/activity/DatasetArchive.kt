package pl.oczadly.baltic.lsc.android.view.dataset.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.converter.AccessTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataStructureEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.entity.DatasetEntity
import pl.oczadly.baltic.lsc.android.view.dataset.service.DatasetService
import pl.oczadly.baltic.lsc.dataset.DatasetApi

class DatasetArchive : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    private val datasetService = DatasetService(
        DatasetApi(MainActivity.state),
        DatasetEntityConverter(),
        DataTypeEntityConverter(),
        DataStructureEntityConverter(),
        AccessTypeEntityConverter(Gson())
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val datasetEntity =
            intent.getSerializableExtra(DatasetView.datasetEntityIntent) as? DatasetEntity
        setContentView(R.layout.activity_delete_resource)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (datasetEntity == null) {
            finish()
        } else {
            findViewById<TextView>(R.id.resource_delete_message_text_view).text =
                "Are you sure you want to archive the dataset?"
            findViewById<TextView>(R.id.resource_delete_name_text_view).text = datasetEntity.name
            findViewById<Button>(R.id.resource_delete_delete_button)
                .setOnClickListener {
                    launch(job) {
                        datasetService.archiveDataset(datasetEntity.uid)
                        finish()
                    }
                }

            findViewById<Button>(R.id.resource_delete_cancel_button)
                .setOnClickListener {
                    finish()
                }
        }
    }
}
