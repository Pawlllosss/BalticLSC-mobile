package pl.oczadly.baltic.lsc.android.view.app.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.datetime.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.datetime.toJavaLocalDateTime
import pl.oczadly.baltic.lsc.android.R
import pl.oczadly.baltic.lsc.android.view.app.entity.AppListItemEntity
import pl.oczadly.baltic.lsc.android.view.app.entity.AppShelfEntity

class AppAdapter(
    private val appShelf: MutableList<AppShelfEntity>,
    private val appsList: MutableList<AppListItemEntity>
) :
    RecyclerView.Adapter<AppAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appNameTextView: TextView = view.findViewById(R.id.app_title)
        val updateDateTextView: TextView = view.findViewById(R.id.app_update_date)
        val descriptionTextView: TextView = view.findViewById(R.id.app_description)
        val imageView: ImageView = view.findViewById(R.id.app_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val app = appsList[position]
        val ownedApps = appShelf.map(AppShelfEntity::unitUid).toSet()
        val paintFlag = if (ownedApps.contains(app.uid)) Paint.UNDERLINE_TEXT_FLAG else 0
        holder.appNameTextView.paintFlags = paintFlag
        holder.appNameTextView.text = app.name
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
    }

    override fun getItemCount() = appsList.size

    fun updateData(appShelves: List<AppShelfEntity>, appsList: MutableList<AppListItemEntity>) {
        this.appShelf.clear()
        this.appShelf.addAll(appShelves)
        this.appsList.clear()
        this.appsList.addAll(appsList)
        notifyDataSetChanged()
    }

    private fun createUpdatedOnText(app: AppListItemEntity): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val newestReleaseDate = getNewestReleaseDate(app)
        val formattedDate: String =
            newestReleaseDate?.toJavaLocalDateTime()?.format(formatter) ?: ""
        return "Updated on $formattedDate"
    }

    private fun getNewestReleaseDate(app: AppListItemEntity): LocalDateTime? = app.releases
        .maxByOrNull { it.date }?.date
}
