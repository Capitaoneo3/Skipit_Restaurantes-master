package br.com.app5m.skipitrestaurantes.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.DialogClickListener
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.dialog_right_message.*


class RightMessageDialog : DialogFragment {
    var message: String
     var clickListener: DialogClickListener? = null

    constructor(message: String, clickListener: DialogClickListener) : super() {
        this.message = message
        this.clickListener = clickListener
    }

    constructor(message: String) : super() {
        this.message = message

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
        return inflater.inflate(R.layout.dialog_right_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(message == "Item adicionado com sucesso."){
//            image.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_cart_outline))
        }
        if(message == "As instruções para alteração de senha foram enviadas para o seu e-mail."){
            image.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_mark_email_read_24))
        }
        messageTextV.text =  message



        coseBt.setOnClickListener {
            dialog?.dismiss()
            clickListener?.onClickListener()

            if (message == "Login efetuado com sucesso.") {
                activity?.let {
                    val intent = Intent(it, HomeActivity::class.java)
                    it.startActivity(intent)
                    it.finishAffinity()
                }
            }
            if (message == "As instruções para alteração de senha foram enviadas para o seu e-mail.") {
               findNavController().navigateUp()
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if(message == "Cadastro adicionado"){

            activity?.let {
                val intent = Intent(it, HomeActivity::class.java)
                it.startActivity(intent)
                it.finishAffinity()
            }

//                findNavController().navigate(R.id.action_productDetailsFrag_to_navigation_cart)
        }
        if (message == "Login efetuado com sucesso.") {
            activity?.let {
                val intent = Intent(it, HomeActivity::class.java)
                it.startActivity(intent)
                it.finishAffinity()
            }
        }
        if (message == "As instruções para alteração de senha foram enviadas para o seu e-mail.") {
            findNavController().navigateUp()
        }
    }

}