package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener

import android.graphics.drawable.Drawable
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_restaurent_history.view.*


class Restaurant_history_adapter(
    private val messageList: List<QueueItem>,
    val clickListener: RecyclerItemClickListener,
    context: Context
) :
    RecyclerView.Adapter<Restaurant_history_adapter.AudioHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class AudioHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val status: TextView = itemView.findViewById(R.id.status)
        val picture: ImageView = itemView.findViewById(R.id.picture)
        val date: TextView = itemView.findViewById(R.id.date)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {

        return AudioHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_restaurent_history, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AudioHolder, position: Int) {
        val queueItem = messageList[position]


//0== restaurant top else restaurant normal= borderless


        holder.name.text = queueItem.nome_fantasia
        Glide
            .with(context)
            .load("queueItem.url")
            .error(R.drawable.nophoto)
            .centerCrop()
            .into(holder.picture)
        holder.date.text = queueItem.data
//        holder.status.text = queueItem.status
        var statusBackground: Drawable = holder.status.background

 /*       if (queueItem.status == "Cancelado") {
            statusBackground.setTint(ContextCompat.getColor(context, R.color.red600))
            holder.status.background = statusBackground

//            holder.status.setBackgroundColor(ContextCompat.getColor(context,R.color.orange400))

        }else{
            holder.status.text = "Concluido"
            statusBackground.setTint(ContextCompat.getColor(context, R.color.green600))
            holder.status.background = statusBackground
        }

        if (queueItem.status == "Concluido") {
            statusBackground.setTint(ContextCompat.getColor(context, R.color.green600))
            holder.status.background = statusBackground
        }else{
            holder.status.text = "Cancelado"
            statusBackground.setTint(ContextCompat.getColor(context, R.color.red600))
            holder.status.background = statusBackground
        }*/




//        holder.itemView.setOnClickListener { clickListener.onClickListenerRestaurantAdapter(message) }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}