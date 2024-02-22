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
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE


class Notify_adapter(
    private val messageList: List<RestaurantDELETE>,
    val clickListener: RecyclerItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<Notify_adapter.AudioHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class AudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val picture: ImageView = itemView.findViewById(R.id.picture)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {

        return AudioHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_notify, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        val message = messageList[position]



            holder.name.text = message.name
            holder.description.text = message.description
 /*       Glide
            .with(context)
            .load(message.image)
            .fitCenter()
            .into(holder.picture)
*/




//        holder.itemView.setOnClickListener { clickListener.onClickListenerRestaurantAdapter(message) }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}