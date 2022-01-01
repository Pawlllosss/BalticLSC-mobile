package pl.oczadly.baltic.lsc.android.view.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.util.formatDate
import pl.oczadly.baltic.lsc.android.view.app.entity.AppReleaseEntity
import pl.oczadly.baltic.lsc.app.action.AppReleaseActionConverter

class AppReleaseAdapter(
    private val appReleases: List<AppReleaseEntity>,
    private val ownedReleasesIds: Set<String>,
    private val context: Context
) : RecyclerView.Adapter<AppReleaseAdapter.ItemViewHolder>() {

    private val releaseActionsConverter: AppReleaseActionConverter = AppReleaseActionConverter()
    private val buttonCreator: ReleaseActionsButtonCreator = ReleaseActionsButtonCreator()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val releaseVersionTextView: TextView = view.findViewById(R.id.app_release_version_text_view)
        val releaseDateTextView: TextView =
            view.findViewById(R.id.app_release_release_date_text_view)
        val releaseStatusTextView: TextView = view.findViewById(R.id.app_release_status_text_view)
        val releaseDescriptionTextView: TextView =
            view.findViewById(R.id.app_release_description_text_view)
        val isOpenSourceTextView: TextView =
            view.findViewById(R.id.app_release_open_source_text_view)
        val buttonsLayout: LinearLayout = view.findViewById(R.id.app_release_buttons_linear_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_release_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val release = appReleases[position]
        val isOwned = ownedReleasesIds.contains(release.releaseUid)
        // TODO: get info from endpoint about isInToolboxFlag
        val actions = releaseActionsConverter.getActionsBasedOnOwnership(
            isOwned,
            false,
            release.releaseStatus
        )
        val buttons = buttonCreator.createButtonsForActions(actions, release, context)

        holder.releaseVersionTextView.text = release.versionName
        holder.releaseDateTextView.text = formatDate(release.date)
        holder.releaseStatusTextView.text = release.releaseStatus.description
        holder.releaseDescriptionTextView.text = release.description
        holder.isOpenSourceTextView.text = if (release.isOpenSource) "Yes" else "No"
        holder.buttonsLayout.removeAllViews()
        buttons.forEach { holder.buttonsLayout.addView(it) }
    }

    override fun getItemCount() = appReleases.size
}
