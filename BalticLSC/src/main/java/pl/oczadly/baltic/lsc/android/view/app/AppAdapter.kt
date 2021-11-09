package pl.oczadly.baltic.lsc.android.view.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.format.DateTimeFormatter
import kotlinx.datetime.toJavaLocalDateTime
import pl.oczadly.baltic.lsc.android.R

class AppAdapter(private val apps: List<AppEntity>) :
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
        val app = apps[position]
        holder.appNameTextView.text = app.name
        holder.updateDateTextView.text = createUpdatedOnText(app)
        app.description?.let { holder.descriptionTextView.text = it }
        holder.imageView.apply {
            Glide.with(context)
                .load(app.icon)
                .into(holder.imageView)
        }
    }

    private fun createUpdatedOnText(app: AppEntity): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDate: String = app.updateDate.toJavaLocalDateTime().format(formatter)
        return "Updated on $formattedDate"
    }

    override fun getItemCount() = apps.size
}