package br.com.app5m.skipitrestaurantes.ui.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.AddresseControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.model.Address
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_menu_address.*
import java.util.ArrayList


class MenuAddressDialog(
    title: String,
    body: String,
    address: Address,
    addressList: ArrayList<Address>
) :BottomSheetDialogFragment() {
    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog
    private var address=address
    private var title=title
    private var body=body
    private var addressList=addressList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNoBackground)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_menu_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()*/
        titleText.text=title
        bodyText.text=body
        clicks()


    }

    private fun clicks() {

        deleteAddressBt.setOnClickListener {
            Message.msg(


                requireContext(), "Apagar Endereço?",
                "", object : Message.Answer {
                    override fun setOnClickListener() {

                        val idDefaultAddress = Preferences(context).getUserLocation()?.id

                        if (idDefaultAddress == address.id ){
                                dialogshowAtention("Troque de endereço antes de apagar o padrão.")
                        }
                        else if(addressList.size==1){
                            dialogshowAtention("Adicione mais um endereço antes de apagar este.")
                        }

                        else{
                            val addressAux = Address()
                            addressAux.id_endereco = address.id
                            AddresseControl(requireContext(),   object : WSResult {
                                override fun responseAddress(addressList: List<Address>, type: String) {
                                    super.responseAddress(addressList, type)
                                    addressList[0].msg?.let { it1 -> dialogshowRight(it1)
                                    }
                                    findNavController().navigate(R.id.action_selectAddressIndexFrag_self)

                                    this@MenuAddressDialog.dismiss()
                                }

                                override fun error(error: String) {
                                    super.error(error)
                                    this@MenuAddressDialog.dismiss()

                                    dialogshowAtention(error)
                                }


                            }).deleteEndereco(addressAux)
                        }






                    }

                })
        }


        editAddressBt.setOnClickListener {

            var bundleAddress = bundleOf("address" to address,"type" to "update"
                )
            findNavController().navigate(R.id.action_selectAddressIndexFrag_to_addressDetailFragment,bundleAddress)
            this.dismiss()

        }
        cancelBt.setOnClickListener {
            this.dismiss()
        }
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
