package br.senai.sp.jandira.contatoretrofit.server

import br.senai.sp.jandira.contatoretrofit.lib.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitAPI {

  companion object {
    private lateinit var instance: Retrofit
    fun getRetrofitIntance(): Retrofit {
      if(!::instance.isInitialized) {
        instance = Retrofit
          .Builder()
          .baseUrl(Constants.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      }
      return instance
    }
  }

}