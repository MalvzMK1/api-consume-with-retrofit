package br.senai.sp.jandira.contatoretrofit.server

import br.senai.sp.jandira.contatoretrofit.model.Contact
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactCall {

  @GET("contacts")
  fun getAllContacts(): Call<List<Contact>>

  @GET("contacts/{id}")
  fun getContactById(@Path("id") id: Int): Call<Contact>

  @POST("contacts")
  fun createContact(@Body contact: Contact): Call<Contact>

  @DELETE("contacts/{id}")
  fun deleteContact(@Path("id") id: Int): Call<Boolean>

  @PUT("contacts/{id}")
  fun updateContact(@Path("id") id: Int, @Body contact: Contact): Call<Boolean>

}