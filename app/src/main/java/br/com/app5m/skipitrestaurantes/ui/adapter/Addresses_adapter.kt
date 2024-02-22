package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.Address


class Addresses_adapter (private val addressList: List<Address>, val clickListener: RecyclerItemClickListener, context: Context):
    RecyclerView.Adapter<Addresses_adapter.ServicesPrevHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()



    class ServicesPrevHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dotsMenu: ImageView = itemView.findViewById(R.id.dotsMenu)
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val FullText: TextView = itemView.findViewById(R.id.FullText)
        val historyAddress: ImageView = itemView.findViewById(R.id.filterHeader2)
        val check: ImageView = itemView.findViewById(R.id.check)
        val defaultVisualize: CardView = itemView.findViewById(R.id.defaultVisualize)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesPrevHolder {

        return ServicesPrevHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_addresses, parent, false))
    }

    override fun onBindViewHolder(holder: ServicesPrevHolder, position: Int) {
        val address = addressList[position]
        holder.titleText.text = address.address+" , "+address.number
        holder.FullText.text = address.address+" , "+address.neighborhood+" , "+address.city+" , "+address.state


        val idDefaultAddress = Preferences(context).getUserLocation()?.id

        if (idDefaultAddress == address.id ){
            holder.historyAddress.visibility = View.INVISIBLE
            holder.check.visibility = View.VISIBLE
        }else{
            holder.historyAddress.visibility = View.VISIBLE
            holder.check.visibility = View.INVISIBLE
            holder.check.visibility = View.INVISIBLE
            holder.defaultVisualize.setBackgroundColor(context.getResources().getColor(R.color.trasparent))
        }



        holder.dotsMenu.setOnClickListener {
            clickListener.onClickListenerAddressesAdapter2(
                holder.titleText.text as String,
                holder.FullText.text as String,address)


        }






        holder.itemView.setOnClickListener { clickListener.onClickListenerAddressesAdapter(address) }///

    }

    override fun getItemCount(): Int {
        return addressList.size
    }

}