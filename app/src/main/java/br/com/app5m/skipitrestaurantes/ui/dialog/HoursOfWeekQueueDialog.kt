package br.com.app5m.skipitrestaurantes.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.ui.adapter.HoursOfWeekQueueAdapter
import kotlinx.android.synthetic.main.dialog_hours_of_week_queue.*

class HoursOfWeekQueueDialog : DialogFragment() {
    private lateinit var hoursOfWeekAdapter: RecyclerView.Adapter<*>
    private val hoursOfWeekListB = java.util.ArrayList<QueueItem>()
    private var searchText :String? = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_hours_of_week_queue,
            container, false
        )
    }

    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNoBackground)
        if (!arguments?.isEmpty!!) {
            var hoursOfWeekList =
                ((arguments?.getSerializable("hoursOfWeekList") as? java.util.ArrayList<QueueItem>)!!)

            hoursOfWeekListB.addAll(hoursOfWeekList)
            if (::hoursOfWeekAdapter.isInitialized){

                hoursOfWeekAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()

        configureRestaurants()

        clicks()
    }

    fun configureRestaurants() {
        hoursOfWeekAdapter = HoursOfWeekQueueAdapter(hoursOfWeekListB, object : RecyclerItemClickListener {



        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        hoursRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            hoursOfWeekAdapter.setHasStableIds(true)


            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_line_bg_vert,
                    null
                )
            )
            hoursRv.addItemDecoration(itemDecoration)




            layoutManager = vmProduct
            adapter = hoursOfWeekAdapter


        }

    }


    private fun clicks() {

    }


    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

}