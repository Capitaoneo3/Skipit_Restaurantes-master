package br.com.app5m.skipitrestaurantes.ui.fragment.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.controller.UserControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.databinding.FragmentLoginBinding
import br.com.app5m.skipitrestaurantes.helper.MyLocation
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.ValidationHelper
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private lateinit var myLocation: MyLocation
    private var preferences: Preferences? = null

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(requireContext())

        myLocation = MyLocation(requireContext())
        preferences?.clearUserLocation()
        if (preferences?.getLocationAux() == "") {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                Handler().postDelayed({
                    if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_permissionLocalizationAct)
                    }
                }, 4000)


            }
        }
        siginUpTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_siginUpFrag)

        }
        recoverPassTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverPassFrag)
        }
        loginBt.setOnClickListener {
            if (!validation()) return@setOnClickListener

            val user = UserItem()
            user.email = binding.emailEt.text.toString()
            user.password = binding.passwordEt.text.toString()



            UserControl(requireContext(),   object : WSResult {
                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)


                    val preferences = Preferences(
                        requireContext()
                    )

                        user[0].msg?.let { it1 -> dialogshowRight(it1) }

                        preferences.setUserData(user[0])
                        preferences.setLogin(true)
                        preferences.clearUserLocation()


                }

                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).login(user)


            //            loadingProgressBar.visibility = View.VISIBLE

        }
    }

    private fun validation(): Boolean {
        return if (!ValidationHelper.email(emailEt, requireContext())) false
        else ValidationHelper.password(passwordEt, requireContext())
    }
    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        dialog.setTargetFragment(this, 1)
        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}