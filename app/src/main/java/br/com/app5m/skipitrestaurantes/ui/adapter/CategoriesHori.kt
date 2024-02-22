package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener

import android.util.DisplayMetrics
import androidx.cardview.widget.CardView
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.model.Category.CategoryItem
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import com.bumptech.glide.Glide


class CategoriesHori (private val categoryList: List<CategoryItem>, val clickListener: RecyclerItemClickListener, context: Context):
    RecyclerView.Adapter<CategoriesHori.CategoriesHoriHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()

    private var screenWidth = 0

    class CategoriesHoriHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val cardView3: CardView = itemView.findViewById(R.id.cardView3)
        val picture: ImageView = itemView.findViewById(R.id.picture)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHoriHolder {
        val displayMetrics = DisplayMetrics()
        (context as HomeActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels

        return CategoriesHoriHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_categories_hori, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesHoriHolder, position: Int) {
        val categoryItem = categoryList[position]
        val itemWidth = screenWidth / 5.33
        val lp = holder.cardView3.layoutParams
        lp.height = lp.height
        lp.width = itemWidth.toInt()

//0== restaurant top else restaurant normal= borderless

            holder.name.text = categoryItem.nome
        Glide
            .with(context)
            .load(WSConstants.categoryImg+ categoryItem.url)
            .error(R.drawable.nophoto)
            .centerCrop()
            .into(holder.picture)

        val civ = holder.picture
    /*    val color: Int = context.getResources().getColor(R.color.appBlue3)
        val cf: ColorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
        civ.setColorFilter(cf)*/



        holder.itemView.setOnClickListener { clickListener.onClickListenerCategoryAdapter(categoryItem) }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

}