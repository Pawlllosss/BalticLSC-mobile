package pl.oczadly.baltic.lsc.android.view.app

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
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.dto.AppShelfItem

class AppStoreView() : Fragment(), CoroutineScope {

    private val job = Job()

    private val appService = AppService(AppApi(MainActivity.state))

    private val appShelfEntityConverter = AppShelfEntityConverter()

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.app_store_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch(Dispatchers.Main) {
            val apps = appService.createFetchAppShelfPromise().value.await()
            val recyclerView = view.findViewById<RecyclerView>(R.id.app_store_recycler_view)
            val appAdapter =
                AppAdapter(convertToAppShelvesEntities(apps).toMutableList())
            recyclerView.adapter = appAdapter

            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.apps_swipe_refresh_layout)
            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val apps = appService.createFetchAppShelfPromise().value.await()
                    appAdapter.updateData(convertToAppShelvesEntities(apps).toMutableList())
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun convertToAppShelvesEntities(apps: List<AppShelfItem>) =
        apps.map(appShelfEntityConverter::convertFromAppShelfItemDTO)
}