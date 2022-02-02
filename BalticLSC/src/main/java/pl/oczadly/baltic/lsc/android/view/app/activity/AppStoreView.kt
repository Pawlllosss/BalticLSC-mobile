package pl.oczadly.baltic.lsc.android.view.app.activity

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
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.adapter.AppAdapter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.AppApi

class AppStoreView : Fragment(), CoroutineScope {

    companion object {
        const val appListItemIntent = "appListItemEntity"
        const val ownedReleasesUidsIntent = "ownedReleases"
        const val appReleaseIntent = "appReleaseEntity"
    }

    private val job = Job()

    private val appService = AppService(
        AppApi(MainActivity.apiConfig, MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_app_store_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch(Dispatchers.Main) {
            val swipeRefreshLayout =
                view.findViewById<SwipeRefreshLayout>(R.id.apps_swipe_refresh_layout)
            swipeRefreshLayout.isRefreshing = true
            val ownedApps = appService.getAppShelf()
            val appList = appService.getAppList()
            val appsSortedByOwnership = appService.sortOwnedAppsFirst(appList, ownedApps)
            swipeRefreshLayout.isRefreshing = false
            val recyclerView = view.findViewById<RecyclerView>(R.id.app_store_recycler_view)
            val appAdapter =
                AppAdapter(
                    appsSortedByOwnership.toMutableList(),
                    ownedApps.toMutableList(),
                    activity!!
                )
            recyclerView.adapter = appAdapter

            view.findViewById<FloatingActionButton>(R.id.app_store_add_app_button)
                .setOnClickListener {
                    val intent = Intent(context, AppCreateView::class.java)
                    startActivity(intent)
                }

            swipeRefreshLayout.setOnRefreshListener {
                launch(job) {
                    val ownedApps = appService.getAppShelf()
                    val appList = appService.getAppList()
                    val appsSortedByOwnership =
                        appService.sortOwnedAppsFirst(appList, ownedApps)
                    appAdapter.updateData(
                        ownedApps.toMutableList(),
                        appsSortedByOwnership.toMutableList()
                    )
                    swipeRefreshLayout.isRefreshing = false
                }
            }
            Log.i("AppStoreView", "View has been created: ${System.nanoTime()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
