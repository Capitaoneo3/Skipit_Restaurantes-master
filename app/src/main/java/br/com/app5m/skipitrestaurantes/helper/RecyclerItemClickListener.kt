package br.com.app5m.skipitrestaurantes.helper

import android.widget.TextView
import br.com.app5m.skipitrestaurantes.model.Address
import br.com.app5m.skipitrestaurantes.model.Category.CategoryItem
import br.com.app5m.skipitrestaurantes.model.Dish
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem


interface RecyclerItemClickListener {

    fun onClickListenerAddressesAdapter(address: Address){
        //optional body
    }
    fun onClickListenerAddressesAdapter2(title: String, fullText: String, address1: Address) {
        //optional body
    }
    fun onClickListenerDishFavAdapter(edish: Dish){
        //optional body
    }

    fun onClickListenerCategoryAdapter(categoryItem: CategoryItem){
        //optional body
    }
    fun onClickListenerRestaurantAdapter(restaurant: RestaurantSubListItem){
        //optional body
    }

    fun onClickListenerHoursOfWeektQueueAdapter(queueItem: QueueItem){
        //optional body
    }
}