package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import android.widget.RatingBar
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import br.com.app5m.skipitrestaurantes.model.Dish


class Dishes_adapter(
    private val truckList: List<Dish>,
    val clickListener: RecyclerItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<Dishes_adapter.ServicesPrevHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class ServicesPrevHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePresc: ImageView = itemView.findViewById(R.id.imagePrescIm)
        val name: TextView = itemView.findViewById(R.id.textView12)
//        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingbar)
//        val ratingtext: TextView = itemView.findViewById(R.id.ratingtext)
//        val favorite_bt: MaterialFavoriteButton = itemView.findViewById(R.id.favorite_bt)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesPrevHolder {

        return ServicesPrevHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_dishes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServicesPrevHolder, position: Int) {
        val estabilishment = truckList[position]


        Glide.with(context)
            .load(estabilishment.image)
            .into(holder.imagePresc)

        holder.name.text = estabilishment.name.toString()
//        holder.favorite_bt.visibility = View.INVISIBLE
/*        holder.favorite_bt.setOnClickListener {
            holder.favorite_bt.isFavorite = !holder.favorite_bt.isFavorite
        }*/
//        holder.favorite_bt.isFavorite = true
//        holder.favorite_bt.setOnClickListener {
//
//            val activity = context as FragmentActivity
//            val fm: FragmentManager = activity.supportFragmentManager
//            val alertDialog = UnFavDialog()
//            alertDialog.show(fm, "fragment_alert")
//
//        }


      /*  holder.ratingBar.rating = estabilishment.rating!!
        holder.ratingtext.text = estabilishment.rating!!.toString()*/

        holder.itemView.setOnClickListener {
            clickListener.onClickListenerDishFavAdapter(
                estabilishment
            )

        }///

    }

    override fun getItemCount(): Int {
        return truckList.size
    }

}