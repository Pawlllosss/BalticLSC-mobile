package pl.oczadly.baltic.lsc.android.view.dataset.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.dataset.adapter.DatasetAdapter
import pl.oczadly.baltic.lsc.android.view.dataset.converter.DatasetEntityConverter
import pl.oczadly.baltic.lsc.android.view.dataset.service.DatasetService
import pl.oczadly.baltic.lsc.dataset.DatasetApi

class DatasetView : Fragment(), CoroutineScope {

    private val job = Job()

    private val datasetService = DatasetService(DatasetApi(MainActivity.state), DatasetEntityConverter())

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
            val datasetEntities = datasetService.getDatasetEntities().toMutableList()

            val datasetAdapter = DatasetAdapter(datasetEntities)
            val recyclerView = view.findViewById<RecyclerView>(R.id.dataset_recycler_view)
            recyclerView.adapter = datasetAdapter

            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.dataset_swipe_refresh_layout)
            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val datasetEntities = datasetService.getDatasetEntities().toMutableList()

                    datasetAdapter.updateData(datasetEntities)
                    swipeRefreshLayout.isRefreshing = false
                }

            }
        }
    }

}
