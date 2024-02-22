package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.AddresseControl
import br.com.app5m.skipitrestaurantes.controller.AddresseControl2
import br.com.app5m.skipitrestaurantes.controller.CompileByCepControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.ValidationHelper
import br.com.app5m.skipitrestaurantes.model.Address
import br.com.app5m.skipitrestaurantes.model.AuxAddresses
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import kotlinx.android.synthetic.main.fragment_address_detail.*
import java.io.IOException


class AddressDetailFragment : Fragment()

{
    private var  isValidZip:Boolean = false
    private var preferences: Preferences? = null
    var addressModel= Address()
    lateinit var user: UserItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(requireContext())
         user = preferences?.getUserData()!!

        var type = arguments?.getString("type")
        var addressModelArgs = arguments?.getParcelable("address") as? Address

        cep_et.setText(addressModelArgs?.cep)
        state_et.setText(addressModelArgs?.state)
        city_et.setText(addressModelArgs?.city)
        address_et.setText(addressModelArgs?.address)
        nbh_et.setText(addressModelArgs?.neighborhood)
        number_et.setText(addressModelArgs?.number)
        complemento_et.setText(addressModelArgs?.complement)


        addButton.setOnClickListener {
            if (!validation()) return@setOnClickListener
            addressModel.id_user =  user.id.toString()
            addressModel.number= number_et.text.toString()
            AddresseControl(requireContext(),   object : WSResult {
                override fun responseAddress(addressList: List<Address>, type: String) {
                    super.responseAddress(addressList, type)
                    addressList[0].msg?.let { it1 -> dialogshowRight(it1)
                    }
                    findNavController().navigateUp()
                }

                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).saveEndereco(addressModel)

        }
        if (type == "add"){
            addButton.visibility = View.VISIBLE
            updateBt.visibility = View.GONE
            makeDefault.visibility = View.GONE
        }else{
            updateBt.visibility = View.VISIBLE
            makeDefault.visibility = View.VISIBLE
            addButton.visibility = View.GONE

            makeDefault.setOnClickListener {
                findNavController().navigateUp()
            }
            updateBt.text = "Atualizar"

        }


        makeDefault.setOnClickListener {

            Message.msg(requireActivity(),"Endereço Padrão",
                "Definir este endereço como Padrão?", object : Message.Answer {
                    override fun setOnClickListener() {

                        preferences?.clearUserLocation()
                        preferences?.setUserLocation(addressModelArgs)
                        dialogshowRight("Endereço padrão atualizado com sucesso!")

                    }
                })

        }



        updateBt.setOnClickListener {
//                Toast.makeText(requireContext(), "111111111111", Toast.LENGTH_SHORT).show()
            if (!validation()) return@setOnClickListener
//                Toast.makeText(requireContext(), "2222222222222", Toast.LENGTH_SHORT).show()



            addressModel.id_endereco = addressModelArgs?.id
            addressModel.latitude = ""
            addressModel.longitude = ""
            addressModel.pt_referencia = ""


            addressModel.cep = cep_et.text.toString().replace("-", "")
            addressModel.state= state_et.text.toString()
            addressModel.city= city_et.text.toString()
            addressModel.address=address_et.text.toString()
            addressModel.neighborhood= nbh_et.text.toString()
            addressModel.number= number_et.text.toString()
            addressModel.complement= complemento_et.text.toString()
            if (type == "add"){

//                    Toast.makeText(requireContext(), "33333333333", Toast.LENGTH_SHORT).show()


            }

            else{
//                    Toast.makeText(requireContext(), "44444444444", Toast.LENGTH_SHORT).show()

                AddresseControl2(requireContext(),   object : WSResult {
                    override fun responseAddress(addressList: AuxAddresses, type: String) {
                        super.responseAddress(addressList, type)
                        addressList.msg?.let { it1 -> dialogshowRight(it1) }

                    }






                    override fun error(error: String) {
                        super.error(error)
                        dialogshowAtention(error)
                    }


                }).updateendereco(addressModel)
            }

        }
        cep_et.addTextChangedListener(object : TextWatcher {
            val handler = Handler()
            var runnable: Runnable=Runnable { }



            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                handler.removeCallbacks(runnable)

            }

            override fun afterTextChanged(s: Editable) {


                runnable = Runnable {
                    val geocoder = Geocoder(requireContext())

                    val zip = cep_et.text.toString()
                    try {
                        val addresses = geocoder.getFromLocationName(zip, 1)
                        if (addresses != null && !addresses.isEmpty()) {
                            isValidZip=true

                            val address = addresses[0]
                            // Use the address as needed
                            val message = String.format(
                                "Latitude: %f, Longitude: %f",
                                address.latitude, address.longitude
                            )
                            val addressName = addresses.get(0)
                                .getAddressLine(0)
                            val country = addresses.get(0).countryName

                            //                            addressModel.address = addressName
                            addressModel.country = country


                        } else {
                            isValidZip=false
                            cep_et.error = "Não foi possível achar o CEP."

                            // Display appropriate message when Geocoder services are not available

                        }
                    } catch (e: IOException) {
                        // handle exception
                    }




                    CompileByCepControl.compile(cep_et.rawText, object :
                        CompileByCepControl.CepResult {
                        override fun resultCep(cep: br.com.app5m.skipitrestaurantes.model.Address) {
                            isValidZip=true

                            if (cep.cep == null) {
                                isValidZip=false

                                cep_et.error = "Por favor, digite um CEP válido."
                                return
                            }
                            addressModel.city = cep.place
                            addressModel.address = cep.logradouro
                            addressModel.state = cep.state
                            //                            addressModel.country = cep.place
                            addressModel.cep = cep.cep
                            addressModel.neighborhood = cep.neighborhood
                            addressModel.complement = cep.complement
                            //                        preferences!!.setUserLocation(addressModel)

                            city_et.setText(cep.place)
                            state_et.setText(cep.state)
                            nbh_et.setText(cep.neighborhood)
                            address_et.setText(cep.logradouro)
                            complemento_et.setText(cep.complement)



                        }
                    })
                    // Do some work with s.toString()
                }
                handler.postDelayed(runnable, 1000)








            }
        })

    }

    private fun validation(): Boolean {
        if(!ValidationHelper.validateTexField(cep_et,requireContext())) return false
//        if (isValidZip)return false
        if (!ValidationHelper.validateTexField(city_et,requireContext())) return false
        if (!ValidationHelper.validateTexField(state_et,requireContext())) return false
        if (!ValidationHelper.validateTexField(nbh_et,requireContext())) return false
        if (!ValidationHelper.validateTexField(number_et,requireContext())) return false
        return ValidationHelper.validateTexField(address_et,requireContext())
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