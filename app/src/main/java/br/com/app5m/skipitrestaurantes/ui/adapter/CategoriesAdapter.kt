package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE

import android.graphics.PorterDuff

import android.graphics.PorterDuffColorFilter
import com.bumptech.glide.Glide


class CategoriesAdapter (private val categoryList: List<RestaurantDELETE>, val clickListener: RecyclerItemClickListener, context: Context):
    RecyclerView.Adapter<CategoriesAdapter.CategoriesHoriHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class CategoriesHoriHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val picture: ImageView = itemView.findViewById(R.id.picture)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHoriHolder {

        return CategoriesHoriHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_categories_big, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesHoriHolder, position: Int) {
        val message = categoryList[position]


//0== restaurant top else restaurant normal= borderless

            holder.name.text = message.name
        Glide
            .with(context)
            .load(message.image)
            .centerCrop()
            .into(holder.picture)

        val civ = holder.picture
        val color: Int = context.getResources().getColor(R.color.colorAccent)
        val cf: ColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        civ.setColorFilter(cf)



//        holder.itemView.setOnClickListener { clickListener.onClickListenerRestaurantAdapter(message) }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}