package com.example.buscacep.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buscacep.model.dados.Endereco
import com.example.buscacep.model.dados.RetroFitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaEndereco(modifier: Modifier = Modifier) {
    var cep by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf<Endereco?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Buscar Endereco")
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("Digite o CEP: ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            isLoading = true
            scope.launch {
                fetchEndereco(cep) { result, error ->
                    endereco = result
                    errorMessage = error
                    isLoading = false
                }
            }
        }) {
            Text("Buscar Endereco")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text(text = "Erro: $errorMessage")
        } else if (endereco != null) {
            Text(
                text = "CEP: ${endereco!!.cep}\n" +
                        "Logradouro: ${endereco!!.logradouro}\n" +
                        "Complemento: ${endereco!!.complemento}\n" +
                        "Bairro: ${endereco!!.bairro}\n" +
                        "Localidade: ${endereco!!.localidade}\n" +
                        "UF: ${endereco!!.uf}\n" +
                        "IBGE: ${endereco!!.ibge}\n" +
                        "GIA: ${endereco!!.gia}\n" +
                        "DDD: ${endereco!!.ddd}\n" +
                        "SIAFI: ${endereco!!.siafi}"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

private fun fetchEndereco(cep: String, callback: (Endereco?, String?) -> Unit) {
    RetroFitClient.enderecoService.getEndereco(cep).enqueue(object : Callback<Endereco> {
        override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
            if (response.isSuccessful && response.body() != null) {
                callback(response.body(), null)
            } else {
                callback(null, "Não foi possível obter o endereço.")
            }
        }

        override fun onFailure(call: Call<Endereco>, t: Throwable) {
            callback(null, "Falha na requisição: ${t.message}")
        }
    })
}
