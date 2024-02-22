package br.com.app5m.skipitrestaurantes.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.DialogClickListener
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.dialog_atention_message.*


class AtentionMessageDialog : DialogFragment {
    var message: String
    var clickListener: DialogClickListener? = null
    var bundle: Bundle? = null

    constructor(message: String, clickListener: DialogClickListener) : super() {
        this.message = message
        this.clickListener = clickListener
    }
    constructor(message: String) : super() {
        this.message = message

    }

    constructor(message: String,bundle: Bundle) : super() {
        this.message = message
        this.bundle = bundle

    }

   

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNoBackground)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_atention_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(message == "erro: 500"){
            messageTextV.text = "Não foi possível atender a sua solicitação. Estamos trabalhando nisso, tente novamente mais tarde."

        }
        else{
            messageTextV.text = message

        }

        coseBt.setOnClickListener {
            clickListener?.onClickListener()

            if(message == "Usuário já está na fila"){
                if (bundle !=null) findNavController().navigate(R.id.action_restaurantDetailFragment_to_queueActivity,bundle)

            }

            dialog?.dismiss()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(message == "Usuário já está na fila"){
            if (bundle !=null) findNavController().navigate(R.id.action_restaurantDetailFragment_to_queueActivity,bundle)

        }
        if (message == "Nenhuma Categoria Encontrada.") {
            findNavController().navigateUp()
        }
    }
}