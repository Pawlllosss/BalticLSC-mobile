package pl.oczadly.baltic.lsc.android.view.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.util.formatDate
import pl.oczadly.baltic.lsc.android.view.app.activity.release.AppReleaseDeleteView
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.app.activity.release.AppReleaseEditView
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.android.view.app.service.AppService
import pl.oczadly.baltic.lsc.app.action.AppReleaseAction
import pl.oczadly.baltic.lsc.app.action.AppReleaseActionConverter

class AppReleaseAdapter(
    private val appReleases: List<AppReleaseEntity>,
    private val ownedReleasesIds: Set<String>,
    private val appService: AppService,
    private val context: Context
) : RecyclerView.Adapter<AppReleaseAdapter.ItemViewHolder>() {

    private val releaseActionsConverter: AppReleaseActionConverter = AppReleaseActionConverter()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val releaseVersionTextView: TextView = view.findViewById(R.id.app_release_version_text_view)
        val releaseDateTextView: TextView =
            view.findViewById(R.id.app_release_release_date_text_view)
        val releaseStatusTextView: TextView = view.findViewById(R.id.app_release_status_text_view)
        val releaseDescriptionTextView: TextView =
            view.findViewById(R.id.app_release_description_text_view)
        val isOpenSourceTextView: TextView =
            view.findViewById(R.id.app_release_open_source_text_view)
        val cockpitButton: Button = view.findViewById(R.id.app_release_cockpit_button)

        //        val toolboxButton: Button = view.findViewById(R.id.app_release_toolbox_button)
        val modificationButtonsLayout: LinearLayout =
            view.findViewById(R.id.app_release_modification_buttons_linear_layout)
        var isOwned: Boolean = false
        var isInToolbox: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_release_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val release = appReleases[position]
        holder.isOwned = ownedReleasesIds.contains(release.releaseUid)
        // TODO: get info from endpoint about isInToolboxFlag
        holder.isInToolbox = false
        val actions = releaseActionsConverter.getActionsBasedOnStatus(
            release.releaseStatus
        )

        holder.releaseVersionTextView.text = release.versionName
        holder.releaseDateTextView.text = formatDate(release.date)
        holder.releaseStatusTextView.text = release.releaseStatus.description
        holder.releaseDescriptionTextView.text = release.description
        holder.isOpenSourceTextView.text = if (release.isOpenSource) "Yes" else "No"
        if (holder.isOwned) holder.cockpitButton.text =
            "Remove from cockpit" else holder.cockpitButton.text = "Add to cockpit"
        holder.cockpitButton.setOnClickListener {
            if (holder.isOwned) {
                holder.cockpitButton.text = "Add to cockpit"
                CoroutineScope(Dispatchers.IO).launch {
                    appService.deleteReleaseFromCockpit(release.releaseUid)
                }
                holder.isOwned = false
            } else {
                holder.cockpitButton.text = "Remove from cockpit"
                CoroutineScope(Dispatchers.IO).launch {
                    appService.addReleaseToCockpit(release.releaseUid)
                }
                holder.isOwned = true
            }
        }

//        if (actions.contains(AppReleaseAction.ADD_TOOLBOX)) {
//
//        } else {
//
//        }


        holder.modificationButtonsLayout.removeAllViews()
        if (actions.contains(AppReleaseAction.EDIT)) {
            val button = Button(context)
            button.text = "Edit"
            button.setOnClickListener {
                val intent = Intent(context, AppReleaseEditView::class.java)
                intent.putExtra(AppStoreView.appReleaseIntent, release)
                context.startActivity(intent)
            }
            holder.modificationButtonsLayout.addView(button)
        }
        if (actions.contains(AppReleaseAction.DELETE)) {
            val button = Button(context)
            button.text = "Delete"
            button.setOnClickListener {
                val intent = Intent(context, AppReleaseDeleteView::class.java)
                intent.putExtra(AppStoreView.appReleaseIntent, release)
                context.startActivity(intent)
            }
            holder.modificationButtonsLayout.addView(button)
        }
    }

    override fun getItemCount() = appReleases.size
}
