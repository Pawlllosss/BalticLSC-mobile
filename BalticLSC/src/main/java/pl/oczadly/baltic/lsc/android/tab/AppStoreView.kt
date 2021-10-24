package pl.oczadly.baltic.lsc.android.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.oczadly.baltic.lsc.android.AppAdapter
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.app.AppApi
import pl.oczadly.baltic.lsc.app.model.App
import pl.oczadly.baltic.lsc.lazyPromise
import kotlin.coroutines.CoroutineContext

class AppStoreView : Fragment(), CoroutineScope {

    private val job = Job()
    private val appApi = AppApi()


    private val apps by lazyPromise {
        withContext(Dispatchers.IO) {
            try {
                return@withContext appApi.fetchApplicationShelf().data
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(activity, "Error when fetching api", Toast.LENGTH_LONG).show()
                return@withContext listOf()
            }
        }
    }

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
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter =
                AppAdapter(
                    activity!!.applicationContext,
                    apps.map { App(it.unit.name, it.unit.icon) })

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