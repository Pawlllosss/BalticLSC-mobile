package pl.oczadly.baltic.lsc.android.view.app.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.activity.AppDetails
import pl.oczadly.baltic.lsc.android.view.app.activity.AppStoreView
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity
import pl.oczadly.baltic.lsc.android.view.app.util.createUpdatedOnText
import pl.oczadly.baltic.lsc.app.dto.list.ReleaseStatus

class AppAdapter(
    private val appsList: MutableList<AppListItemEntity>,
    private val appShelf: MutableList<AppShelfEntity>,
    private val context: Context
) :
    RecyclerView.Adapter<AppAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appNameTextView: TextView = view.findViewById(R.id.app_title)
        val updateDateTextView: TextView = view.findViewById(R.id.app_update_date)
        val descriptionTextView: TextView = view.findViewById(R.id.app_description)
        val imageView: ImageView = view.findViewById(R.id.app_image)
        val appDetailsButton: Button = view.findViewById(R.id.app_details_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val app = appsList[position]
        val ownedApps = appShelf.map(AppShelfEntity::unitUid).toSet()
        val isOwned = ownedApps.contains(app.uid)
        val paintFlag = if (isOwned) Paint.UNDERLINE_TEXT_FLAG else 0
        holder.appNameTextView.paintFlags = paintFlag
        holder.appNameTextView.text = createAppName(app, isOwned)
        holder.updateDateTextView.text = createUpdatedOnText(app)
        if (app.shortDescription != null) {
            holder.descriptionTextView.text = app.shortDescription
        } else {
            holder.descriptionTextView.text = null
        }
        holder.imageView.apply {
            Glide.with(context)
                .load(app.iconUrl)
                .into(holder.imageView)
        }
        val ownedReleases = appShelf.map { AppShelfEntity::releaseUid }.toSet()
        holder.appDetailsButton.setOnClickListener {
            val intent = Intent(context, AppDetails::class.java)
            intent.putExtra(AppStoreView.appListItemIntent, app)
            intent.putExtra(AppStoreView.ownedReleasesUidsIntent, HashSet(ownedReleases))
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = appsList.size

    fun updateData(appShelves: List<AppShelfEntity>, appsList: MutableList<AppListItemEntity>) {
        this.appShelf.clear()
        this.appShelf.addAll(appShelves)
        this.appsList.clear()
        this.appsList.addAll(appsList)
        notifyDataSetChanged()
    }

    private fun createAppName(app: AppListItemEntity, isOwned: Boolean): String {
        val appName = app.name
        if (!isOwned && app.releases.any { it.releaseStatus == ReleaseStatus.APPROVED || it.releaseStatus == ReleaseStatus.CREATED }) {
            return "$appName (Can be added)"
        } else if (isOwned) {
            return "$appName (Owned)"
        }

        return appName
    }
}
