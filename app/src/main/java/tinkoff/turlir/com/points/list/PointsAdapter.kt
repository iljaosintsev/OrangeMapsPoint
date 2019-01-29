package tinkoff.turlir.com.points.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.maps.MapsPoint
import tinkoff.turlir.com.points.storage.PointPicturable

class PointsAdapter(private val dpi: String) : RecyclerView.Adapter<PointsAdapter.ViewHolder>() {

    private val list = arrayListOf<PointPicturable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.point_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val point = list[position]
        holder.bind(point.point)
        holder.bind(point.picture(dpi))
    }

    override fun getItemCount() = list.size

    fun replace(now: List<PointPicturable>) {
        val diff = DiffUtil.calculateDiff(MapPointDiffer(list, now))
        diff.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(now)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val avatar = itemView.findViewById<ImageView>(R.id.frg_map_icon)
        private val partnerName = itemView.findViewById<TextView>(R.id.frg_map_partner)
        private val identity = itemView.findViewById<TextView>(R.id.frg_map_id)
        private val fullAddress = itemView.findViewById<TextView>(R.id.frg_map_full_address)
        private val coordinates = itemView.findViewById<TextView>(R.id.frg_map_coord)

        init {
            val stub = ContextCompat.getColor(avatar.context, R.color.colorPrimary)
            avatar.setBackgroundColor(stub)
        }

        fun bind(point: MapsPoint) {
            partnerName.text = point.partnerName
            identity.text = point.externalId
            fullAddress.text = point.fullAddress
            coordinates.text = point.location.toString()
        }

        fun bind(url: String) {
            Picasso.with(avatar.context)
                .load(url)
                .into(avatar)
        }
    }

    class MapPointDiffer(
        private val old: List<PointPicturable>,
        private val now: List<PointPicturable>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
            val (oldItem) = old[oldIndex]
            val (nowItem) = now[newIndex]
            return oldItem.externalId == nowItem.externalId
        }

        override fun areContentsTheSame(oldIndex: Int, newIndex: Int): Boolean {
            val oldItem = old[oldIndex]
            val nowItem = now[newIndex]
            return oldItem == nowItem
        }

        override fun getOldListSize() = old.size

        override fun getNewListSize() = now.size
    }
}