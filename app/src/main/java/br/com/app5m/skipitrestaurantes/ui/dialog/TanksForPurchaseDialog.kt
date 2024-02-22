package br.com.app5m.skipitrestaurantes.ui.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.dialog_tanks_for_purchase.*


class TanksForPurchaseDialog(context: Context,msg:String) : DialogFragment() {

    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private var msg= msg
    private var context2= context



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNoBackground)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_tanks_for_purchase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView5.text =msg
        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()

        clicks()


    }

    private fun clicks() {
        coseBt.setOnClickListener {
            if (msg == "A espera acabou. Você completou a fila!" ){
                val myIntent = Intent(context2, HomeActivity::class.java)
                requireActivity().finishAffinity()
                this.startActivity(myIntent)
            }
            this.dismiss()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        if (msg == "A espera acabou. Você completou a fila!" ){
            val myIntent = Intent(context2, HomeActivity::class.java)
            requireActivity().finishAffinity()
            this.startActivity(myIntent)
        }
    }

}