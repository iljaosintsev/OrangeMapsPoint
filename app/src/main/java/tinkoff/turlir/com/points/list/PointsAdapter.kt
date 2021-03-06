package tinkoff.turlir.com.points.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.storage.PointPicturable

class PointsAdapter(private val dpi: String, private val callback: PointClickCallback) : RecyclerView.Adapter<PointInfoHolder>() {

    private val list = arrayListOf<PointPicturable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointInfoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.point_item, parent, false)
        val holder = PointInfoHolder(view)
        holder.buttonOpen.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                val point = list[holder.adapterPosition]
                callback.openPoint(holder.adapterPosition, point, holder.avatar, holder.partnerName)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: PointInfoHolder, position: Int) {
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

    interface PointClickCallback {

        fun openPoint(
            adapterPosition: Int,
            point: PointPicturable,
            avatar: View,
            title: View
        )
    }
}