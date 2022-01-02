package pl.oczadly.baltic.lsc.android.view.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import pl.oczadly.baltic.lsc.android.MainActivity
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.adapter.AppReleaseAdapter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppListItemEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.converter.AppShelfEntityConverter
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.android.view.app.util.createUpdatedOnText
import pl.oczadly.baltic.lsc.app.AppApi

class AppDetails : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    // TODO: remove if won't be used
    private val appService = AppService(
        AppApi(MainActivity.state),
        AppListItemEntityConverter(),
        AppShelfEntityConverter()
    )

    override val coroutineContext: CoroutineContext
        get() = job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_details)
        setSupportActionBar(findViewById(R.id.toolbar))
        val appListItem =
            intent.getSerializableExtra(AppStoreView.appListItemIntent) as? AppListItemEntity
        val ownedReleasesUids =
            intent.getSerializableExtra(AppStoreView.ownedReleasesUidsIntent) as? HashSet<String>
        if (appListItem == null || ownedReleasesUids == null) {
            finish()
        } else {
            findViewById<ImageView>(R.id.app_details_image).let {
                Glide.with(it.context)
                    .load(appListItem.iconUrl)
                    .into(it)
            }
            findViewById<TextView>(R.id.app_details_title).text = appListItem.name
            findViewById<TextView>(R.id.app_details_description).text = appListItem.longDescription
            findViewById<TextView>(R.id.app_details_update_date).text =
                createUpdatedOnText(appListItem)

            findViewById<RecyclerView>(R.id.app_details_releases_recycler_view).adapter =
                AppReleaseAdapter(appListItem.releases, ownedReleasesUids, applicationContext)

            findViewById<Button>(R.id.app_details_cancel_button)
                .setOnClickListener {
                    finish()
                }

            findViewById<Button>(R.id.app_details_edit_button)
                .setOnClickListener {
                    val intent = Intent(this, AppEditView::class.java)
                    intent.putExtra(AppStoreView.appListItemIntent, appListItem)
                    startActivity(intent)
                }

            // TODO: need to make sure that there's no releases attached
            findViewById<Button>(R.id.app_details_delete_button)
                .setOnClickListener {
                    val intent = Intent(this, AppDeleteView::class.java)
                    intent.putExtra(AppStoreView.appListItemIntent, appListItem)
                    startActivity(intent)
                    finish()
                }

            findViewById<Button>(R.id.app_details_release_create_button)
                .setOnClickListener {
                    val intent = Intent(this, AppReleaseCreateView::class.java)
                    intent.putExtra(AppStoreView.appListItemIntent, appListItem)
                    startActivity(intent)
                }
        }
    }
}
