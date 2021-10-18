package pl.oczadly.baltic.lsc.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.oczadly.baltic.lsc.app.model.App

class AppAdapter(private val context: Context, private val apps : List<App>): RecyclerView.Adapter<AppAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val app = apps[position]
        holder.textView.text = app.name
        holder.imageView.apply {
            Glide.with(context)
                .load(app.icon)
                .into(holder.imageView)
        }
    }

    override fun getItemCount() = apps.size
}