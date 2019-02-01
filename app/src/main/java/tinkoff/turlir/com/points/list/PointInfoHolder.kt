package tinkoff.turlir.com.points.list

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import tinkoff.turlir.com.points.R
import tinkoff.turlir.com.points.maps.MapsPoint

class PointInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val buttonOpen: Button = itemView.findViewById(R.id.frg_map_open)
    val avatar: ImageView = itemView.findViewById(R.id.frg_map_icon)
    val partnerName = itemView.findViewById<TextView>(R.id.frg_map_partner)

    private val identity = itemView.findViewById<TextView>(R.id.frg_map_id)
    private val fullAddress = itemView.findViewById<TextView>(R.id.frg_map_full_address)
    private val coordinates = itemView.findViewById<TextView>(R.id.frg_map_coord)

    fun bind(point: MapsPoint) {
        partnerName.text = point.partnerName
        identity.text = point.externalId
        fullAddress.text = point.fullAddress
        coordinates.text = point.location.toString()
    }

    fun bind(url: String) {
        Picasso.with(avatar.context)
            .load(url)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.partner_stub)
            .into(avatar)
    }
}