package br.com.app5m.skipitrestaurantes.ui.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.UserControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.databinding.FragmentRecoverPassBinding
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.ValidationHelper
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import kotlinx.android.synthetic.main.fragment_recover_pass.*


class RecoverPassFrag : Fragment()


{
    private var _binding: FragmentRecoverPassBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder

    private lateinit var alertDialog: AlertDialog

//    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverPassBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        preferences = Preferences(requireContext())

        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()

        clicks()


    }

    fun clicks() {



        binding.sendBt.setOnClickListener {

            if (!validation()) return@setOnClickListener

            val user = UserItem()
            user.email = binding.email.text.toString()


            UserControl(requireContext(),   object : WSResult {
                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)


                    val preferences = Preferences(
                        requireContext()
                    )
                    if (user[0].status =="01"){
                        user[0].msg?.let { it1 -> dialogshowRight(it1) }

                    }else{
                        user[0].msg?.let { it1 -> dialogshowAtention(it1) }

                    }
                }


                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).recuperarSenha(user)

        }

    }
    private fun validation(): Boolean {
        if (!ValidationHelper.email(binding.email, requireContext())) return false
        return true
    }
    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    //to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}