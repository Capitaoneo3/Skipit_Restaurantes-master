package br.com.app5m.skipitrestaurantes.ui.fragment.main.home.restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.QueueControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.model.fila.Queue
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.ui.activity.LoginActivity
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_restaurant_detail.*


class RestaurantDetailFragment : Fragment() {
    private var estabArgs: RestaurantSubListItem? = null
    private var preferences: Preferences? = null
    private var id_fila: Int? = null
    private var id_horario: Int? = null
    private val listhorariosfila = java.util.ArrayList<QueueItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        preferences = Preferences(requireContext())
        if (!arguments?.isEmpty!!) {
            estabArgs = (arguments?.getParcelable("restaurantArgs") as? RestaurantSubListItem)

            listhorariosfiladia()
            consultQueueSize()
            listhorariosfila()
        }

        requireActivity().main_toolbar.visibility = View.VISIBLE
    }

    fun consultQueueSize() {

        var queueModel = QueueItem()
        queueModel.id_para = estabArgs?.id
        queueModel.status = 1
        QueueControl(requireContext(), object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                queueSizeText.text = queue[0].rows.toString() + " na fila"
            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).pessoasnafilaqnt(queueModel)

    }

    fun listhorariosfila() {
        var queueModel = QueueItem()
        queueModel.id_para = estabArgs?.id
        QueueControl(requireContext(), object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                listhorariosfila.addAll(queue)
                var bundle = bundleOf(
                    "hoursOfWeekList" to listhorariosfila
                )
                infoQueue.setOnClickListener {

                    findNavController().navigate(
                        R.id.action_restaurantDetailFragment_to_hoursOfWeekQueueDialog,
                        bundle
                    )
                }
            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).listhorariosfila(queueModel)
    }

    fun inserirUserfila() {
        /*   "id_de": 900,
           "id_fila": 11,
           "id_horario": 7,
           "id_para": 874,
           "status": 3,
           "token": "xvCAmyyPGh"   */
        var queueModel = QueueItem()

        queueModel.id_de = preferences?.getUserData()?.id
        queueModel.id_fila = id_fila
        queueModel.id_horario = id_horario
        queueModel.id_para = estabArgs?.id
        queueModel.status = 1

        QueueControl(requireContext(), object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                var modelB = QueueItem()
                modelB.posicao = queue[0].posicao
                modelB.nome_fantasia = estabArgs?.nome_fantasia
                modelB.id_filas_c = queue[0].id
                modelB.id_de = preferences?.getUserData()?.id
                modelB.id_para = estabArgs?.id
                modelB.id_horario = id_horario
                modelB.status = 1
                var bundle = bundleOf(
                    "bundleQueue" to modelB
                )
                if (queue[0].status == 4) {
                    queue[0].msg?.let { dialogshowAtention2(it, bundle) }

                }
                if (queue[0].status == 1) {


                    findNavController().navigate(
                        R.id.action_restaurantDetailFragment_to_queueActivity,
                        bundle
                    )
                }


            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }


        }).inserirUserfila(queueModel)
    }

    fun listhorariosfiladia() {

        var queueModel = QueueItem()
        queueModel.id_para = estabArgs?.id
        QueueControl(requireContext(), object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)
                id_horario = queue[0].id_horario
                id_fila = queue[0].id_fila
                goQueue.setOnClickListener {
                    if (preferences?.getLogin() == false) {
                        goLogin()

                    } else if (queue[0].rows == 0) {
                        dialogshowAtention("Não temos fila neste horário.")
                    } else {
                        Message.msg(
                            requireContext(), "Entrar na Fila?",
                            "", object : Message.Answer {

                                override fun setOnClickListener() {
                                    inserirUserfila()


                                }
                            })
                    }

                }

            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).listhorariosfiladia(queueModel)


    }

    fun dialogshowAtention2(message: String, bundle: Bundle) {
        val dialog = AtentionMessageDialog(message,bundle)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!arguments?.isEmpty!!) {

            estabArgs = (arguments?.getParcelable("restaurantArgs") as? RestaurantSubListItem)


            name.text = estabArgs?.nome_fantasia
            if (!estabArgs?.endereco.isNullOrEmpty()) {
                adress.text =
                    estabArgs?.endereco + ", " + estabArgs?.numero + ", " + estabArgs?.bairro

            }
            if (!estabArgs?.distancia.isNullOrEmpty()) {
                kmText.text = estabArgs?.distancia

            } else kmText.text = "0 Km"


            Glide.with(requireContext())
                .load(WSConstants.avatarImg + estabArgs?.avatar)
                .error(R.drawable.nophoto)

                .into(avatarPerfil_iv)
        }
        menuFoodBt.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantDetailFragment_to_foodMenuEstabelishment)

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

}