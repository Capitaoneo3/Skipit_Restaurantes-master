package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.UserControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.activity.LoginActivity
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFrag : Fragment()


{
    private var preferences: Preferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onResume() {
        super.onResume()
        preferences = Preferences(requireContext())

        if (preferences?.getLogin() == true) {
            loginOrlogoutText.text = "Sair"

            Glide.with(requireContext())
                .load(R.drawable.ic_baseline_power_settings_new_24)
                .into(logoutIgm)


            preferences = Preferences(
                requireContext()
            )
            var user = preferences?.getUserData()
            if (user != null) updateView()

            ///////


            ////////
            if (user != null) {
                Glide.with(requireContext())
                    .load(WSConstants.avatarImg + user.avatar)    .error(R.drawable.nophoto)

                    .into(profile_image)

            }

            if (user != null) {
                name.text = user.nome
            }



        } else {

            loginOrlogoutText.text = "Entrar"
            name.text = ""
            Glide.with(requireContext())
                .load(R.drawable.nophoto)
                .into(profile_image)
            Glide.with(requireContext())
                .load(R.drawable.ic_baseline_login_24)
                .into(logoutIgm)


        }



        profileMenu.setOnClickListener {
            if (preferences?.getLogin() == false) {
                goLogin()

            }else            findNavController().navigate(R.id.action_menuFrag_to_profileFrag)

        }
        adressesMenu.setOnClickListener {

            if (preferences?.getLogin() == false) {
                goLogin()

            }else            findNavController().navigate(R.id.action_menuFrag_to_selectAddressIndexFrag)

        }

        categoriesMenu.setOnClickListener {
            if (preferences?.getLogin() == false) {
                goLogin()

            }else            findNavController().navigate(R.id.action_menuFrag_to_categoriesFrag)

        }

        categoriesMenu.setOnClickListener {
                   findNavController().navigate(R.id.action_menuFrag_to_categoriesFrag)

        }

        historyMenu.setOnClickListener {
            if (preferences?.getLogin() == false) {
                goLogin()

            }else            findNavController().navigate(R.id.action_menuFrag_to_historyFrag)

        }




        exitAppMenu.setOnClickListener {

            if (preferences?.getLogin() == true) {
                Message.msg(
                    requireContext(), "Sair do Aplicativo?",
                    "", object : Message.Answer {
                        override fun setOnClickListener() {
                            preferences?.clearUserData()
                            findNavController().navigate(R.id.action_menuFrag_to_mainActivity)


                        }
                    })
            }else{
                goLogin()

            }


        }

    }


    fun goLogin(){
        Message.msg(
            requireContext(), "Fazer Login?",
            "", object : Message.Answer {
                override fun setOnClickListener() {
                    requireActivity().finishAffinity()
                    activity?.let {
                        val intent = Intent(it, LoginActivity::class.java)
                        it.startActivity(intent)
                        it.finishAffinity()
                    }

                }
            })
    }
    private  fun updateView() {

        val userAux2= preferences?.getUserData()
        val userAux= UserItem()
        userAux.id_user = userAux2?.id

        userAux.let {
            UserControl(requireContext(),   object : WSResult {

                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)
                    val preferences = Preferences(
                        requireContext()
                    )
                    user[0].msg?.let { it1 -> dialogshowRight(it1) }


                    preferences.setUserData(user[0])

                    Glide.with(requireContext())
                        .load(WSConstants.avatarImg+ user[0].avatar)
                        .error(R.drawable.nophoto)
                        .into(profile_image)

                }


                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).listId(it)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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