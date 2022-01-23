package pl.oczadly.baltic.lsc.android.view.dataset.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.activity.form.DatasetAddForm
import pl.oczadly.baltic.lsc.android.view.dataset.adapter.DatasetAdapter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.AccessTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataStructureEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DataTypeEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.service.DatasetService
import pl.oczadly.baltic.lsc.dataset.DatasetApi

class DatasetView : Fragment(), CoroutineScope {

    companion object {
        const val datasetEntityIntent = "datasetEntityIntent"
        const val dataTypeListIntent = "dataTypeListIntent"
        const val dataStructureListIntent = "dataStructureListIntent"
        const val accessTypeListIntent = "accessTypeListIntent"
    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_dataset_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch(Dispatchers.Main) {
            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.dataset_swipe_refresh_layout)
            swipeRefreshLayout.isRefreshing = true
            val datasetEntities = datasetService.getDatasets().toMutableList()
            val dataTypes = datasetService.getDataTypes()
            val dataStructures = datasetService.getDataStructures()
            val accessTypes = datasetService.getAccessTypes()
            swipeRefreshLayout.isRefreshing = false

            val datasetAdapter =
                DatasetAdapter(datasetEntities, dataTypes, dataStructures, accessTypes)
            val recyclerView = view.findViewById<RecyclerView>(R.id.dataset_recycler_view)
            recyclerView.adapter = datasetAdapter

            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val datasetEntities = datasetService.getDatasets().toMutableList()

                    datasetAdapter.updateData(datasetEntities)
                    swipeRefreshLayout.isRefreshing = false
                }

            }

            view.findViewById<FloatingActionButton>(R.id.dataset_add_button)
                .setOnClickListener {
                    val intent = Intent(context, DatasetAddForm::class.java)
                    intent.putExtra(accessTypeListIntent, ArrayList(accessTypes))
                    intent.putExtra(dataTypeListIntent, ArrayList(dataTypes))
                    intent.putExtra(dataStructureListIntent, ArrayList(dataStructures))
                    context?.startActivity(intent)
                }
            Log.i("DatasetView", "View has been created: ${System.nanoTime()}")
        }
    }

}
