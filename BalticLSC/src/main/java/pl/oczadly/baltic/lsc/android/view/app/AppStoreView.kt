package pl.oczadly.baltic.lsc.android.view.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.lazyPromise

class AppStoreView() : Fragment(), CoroutineScope {

    private val job = Job()

    // TODO: move that to view model
    // TODO: check in what context should it run
    private val appApi = AppApi(MainActivity.state)
    private val apps by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: fix toast there or use either?
//                Toast.makeText(activity, "Error when fetching api", Toast.LENGTH_LONG).show()
                return@withContext listOf()
            }
        }
    }

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
            val apps = apps.await()
            val recyclerView = view.findViewById<RecyclerView>(R.id.app_store_recycler_view)
            recyclerView.adapter =
                AppAdapter(apps.map(appShelfEntityConverter::convertFromAppShelfItemDTO))

            // Use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}