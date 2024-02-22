package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import com.bumptech.glide.Glide


class Restaurant_adapter(
    private val restaurantList: List<RestaurantSubListItem>,
    val clickListener: RecyclerItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<Restaurant_adapter.AudioHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class AudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCardView: CardView = itemView.findViewById(R.id.mainCardView)
        val name: TextView = itemView.findViewById(R.id.name)
        val adress: TextView = itemView.findViewById(R.id.adress)
        val rateText: TextView = itemView.findViewById(R.id.rateText)
        val kmText: TextView = itemView.findViewById(R.id.kmText)
        val picture: ImageView = itemView.findViewById(R.id.picture)
        val rating_bar_small: RatingBar = itemView.findViewById(R.id.rating_bar_small)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {

        return AudioHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_restaurant, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        val restaurantSubListItem = restaurantList[position]


//0== restaurant top else restaurant normal= borderless
//        holder.mainCardView.elevation = 0.0F

            holder.name.text = restaurantSubListItem.nome_fantasia
        if (!restaurantSubListItem.endereco.isNullOrEmpty()){
            holder.adress.text = restaurantSubListItem?.endereco+", "+restaurantSubListItem?.numero+", "+restaurantSubListItem?.bairro
        }
//            holder.rateText.text = "(" + restaurantSubListItem.stairs.toString() + ")"
        if (!restaurantSubListItem?.distancia.isNullOrEmpty()){
            holder.kmText.text=restaurantSubListItem?.distancia

        }else  holder.kmText.text="0 Km"

        Glide
                .with(context)
                .load(WSConstants.avatarImg+ restaurantSubListItem.avatar)
                .error(R.drawable.nophoto)
                .centerCrop()
                .into(holder.picture)
//            holder.rating_bar_small.rating = restaurantSubListItem.stairs!!





        holder.itemView.setOnClickListener { clickListener.onClickListenerRestaurantAdapter(restaurantSubListItem) }

    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

}