package com.example.buscacep.model.dados

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {
        private const val BASE_URL = "http://viacep.com.br/ws/"

    val enderecoService: EnderecoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EnderecoService::class.java)
    }
}