package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE
import com.bumptech.glide.Glide

class Fav_restaurant_adapter(
    private val messageList: List<RestaurantDELETE>,
    val clickListener: RecyclerItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<Fav_restaurant_adapter.AudioHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class AudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val adress: TextView = itemView.findViewById(R.id.adress)
        val rateText: TextView = itemView.findViewById(R.id.rateText)
        val kmText: TextView = itemView.findViewById(R.id.kmText)
        val picture: ImageView = itemView.findViewById(R.id.picture)
        val rating_bar_small: RatingBar = itemView.findViewById(R.id.rating_bar_small)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {

        return AudioHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_favorites, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        val message = messageList[position]


//0== restaurant top else restaurant normal= borderless
        if (message.id == "0") {

//            holder.animationView.playAnimation()
            holder.name.text = message.name
            holder.adress.text = message.endereco
            holder.rateText.text = "(" + message.stairs.toString() + ")"
            holder.kmText.text = message.km + " Km"

            Glide
                .with(context)
                .load(message.image)
                .centerCrop()
                .into(holder.picture)
            holder.rating_bar_small.rating = message.stairs!!


        } else {
            holder.name.text = message.name
            holder.adress.text = message.endereco
            holder.rateText.text = "(" + message.stairs.toString() + ")"
            holder.kmText.text = message.km + " Km"

            Glide
                .with(context)
                .load(message.image)
                .centerCrop()
                .into(holder.picture)
            holder.rating_bar_small.rating = message.stairs!!

        }




//        holder.itemView.setOnClickListener { clickListener.onClickListenerRestaurantAdapter(message) }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}