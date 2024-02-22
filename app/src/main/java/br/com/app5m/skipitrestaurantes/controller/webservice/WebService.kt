package br.com.app5m.skipitrestaurantes.controller.webservice



import br.com.app5m.skipitrestaurantes.controller.BuscaEstabControl
import br.com.app5m.skipitrestaurantes.model.Address
import br.com.app5m.skipitrestaurantes.model.AuxAddresses
import br.com.app5m.skipitrestaurantes.model.Category.Category
import br.com.app5m.skipitrestaurantes.model.Category.CategoryItem
import br.com.app5m.skipitrestaurantes.model.Fcm
import br.com.app5m.skipitrestaurantes.model.fila.Queue
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.model.restaurant.Restaurant
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubList
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface WebService {
    //USER
    @POST("usuarios/loginapp/")
    fun login(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/cadastroapp/")
    fun cadastrar(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/recuperarsenha/")
    fun recuperarsenha(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/updatepassword/")
    fun updatepassword(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/verificatoken/")
    fun verificatoken(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/updatepasswordtoken/")
    fun updatepasswordtoken(@Body u: UserItem): Call<User>


    //cep
    @GET("{CEP}/json/")
    fun getAddressByCEP(@Path("CEP") CEP: String?): Call<Address>

    //RESTAURANT
    @POST("usuarios/busca/")
    fun buscaEstab(@Body u: RestaurantSubListItem): Call<Restaurant>

    //RESTAURANT
    @POST("usuarios/listestabcateg/")
    fun buscaPorCategoria(@Body u: RestaurantSubListItem): Call<RestaurantSubList>

    //USER
    @POST("usuarios/updatecadastroapp/")
    fun updateCadastro(@Body u: UserItem): Call<User>

    //USER
    @POST("usuarios/listid/")
    fun listId(@Body u: UserItem): Call<User>

    //FCM

    @POST("usuarios/savefcm/")
    fun saveFcm(@Body u: Fcm): Call<Fcm?>

    //ENDERECO
    @POST("usuarios/saveendereco/")
    fun saveEndereco(@Body address: Address): Call<List<Address>>

    //ENDERECO
    @POST("usuarios/removeendereco/")
    fun deleteendereco(@Body address: Address): Call<List<Address>>

    //ENDERECO
    @POST("usuarios/listenderecos/")
    fun listenderecoid( @Body user: UserItem): Call<List<Address>>

    //ENDERECO
    @POST("usuarios/updateendereco/")
    fun updateendereco(@Body address: Address): Call<AuxAddresses>

    //USER
  /*  @Multipart
    @POST("usuarios/updateavatar/")
    fun updateavatar(
        @Path id_user: MultipartBody.Part,
        @Part avatar: MultipartBody.Part,
        @Part token: MultipartBody.Part
    ): Call<User>*/

    //ENDERECO
    @POST("cardapio/listcategorias/")
    fun listCategories(@Body categoryBody: CategoryItem): Call<Category>

    //FILA
    @POST("horarios/pessoasnafilaqnt/")
    fun pessoasnafilaqnt(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/listhorariosfiladia/")
    fun listhorariosfiladia(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/listhorariosfila/")
    fun listhorariosfila(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/inserirfila/")
    fun inserirUserfila(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/entrarnafila/")
    fun atualizarUsuarioNaFila(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/posicaonafila/")
    fun posicaonafila(@Body queueBody: QueueItem): Call<Queue>

    //FILA
    @POST("horarios/historiconafila/")
    fun historiconafila(@Body queueBody: QueueItem): Call<Queue>


}