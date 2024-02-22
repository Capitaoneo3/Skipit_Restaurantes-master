package br.com.app5m.skipitrestaurantes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener

import br.com.app5m.skipitrestaurantes.model.fila.QueueItem


class HoursOfWeekQueueAdapter (private val list: List<QueueItem>, val clickListener: RecyclerItemClickListener, context: Context):
    RecyclerView.Adapter<HoursOfWeekQueueAdapter.CategoriesHoriHolder>() {

    val context = context
    private val viewPool = RecyclerView.RecycledViewPool()


    class CategoriesHoriHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day_name: TextView = itemView.findViewById(R.id.day_name)
        val horario_in: TextView = itemView.findViewById(R.id.horario_in)
        val horario_out: TextView = itemView.findViewById(R.id.horario_out)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesHoriHolder {

        return CategoriesHoriHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_queue_hours_of_week, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoriesHoriHolder, position: Int) {
        val list = list[position]



            holder.day_name.text = list.day_name
            holder.horario_in.text = list.horario_in
            holder.horario_out.text = list.horario_out




        holder.itemView.setOnClickListener { clickListener.onClickListenerHoursOfWeektQueueAdapter(list) }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}