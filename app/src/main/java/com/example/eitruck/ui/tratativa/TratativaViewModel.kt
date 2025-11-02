package com.example.eitruck.ui.tratativa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eitruck.data.remote.repository.postgres.RecordRepository
import com.example.eitruck.model.TratativaRequest
import com.example.eitruck.model.TratativaResponse
import kotlinx.coroutines.launch

class RecordViewModel : ViewModel() {

    private var repository: RecordRepository? = null

    private val _carregando = MutableLiveData<Boolean>()
    val carregandoLiveData: LiveData<Boolean> get() = _carregando

    private val _tratativaCriada = MutableLiveData<TratativaResponse?>()
    val tratativaCriada: LiveData<TratativaResponse?> get() = _tratativaCriada

    private val _tratativas = MutableLiveData<List<TratativaResponse>?>()
    val tratativas: LiveData<List<TratativaResponse>?> get() = _tratativas

    private val _tratativaSelecionada = MutableLiveData<TratativaResponse?>()
    val tratativaSelecionada: LiveData<TratativaResponse?> get() = _tratativaSelecionada

    fun setToken(token: String) {
        repository = RecordRepository(token)
    }

    fun criarTratativa(idViagem: Int, idMotorista: Int, texto: String) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val request = TratativaRequest(
                    idViagem = idViagem,
                    idMotorista = idMotorista,
                    tratativa = texto,
                    transactionMade = "Criação via app"
                )
                val response = repository?.createRecord(request)
                if (response != null) _tratativaCriada.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }

    fun atualizarTratativaPorMotorista(idViagem: Int, idMotorista: Int, texto: String) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val response = repository?.updateRecordByDriver(idViagem, idMotorista, texto)
                if (response != null) _tratativaCriada.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }

    fun buscarTodasTratativas() {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val response = repository?.getAllRecords()
                if (response != null) _tratativas.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }

    fun buscarTratativaPorId(id: Int) {
        viewModelScope.launch {
            _carregando.value = true
            try {
                val response = repository?.getRecordById(id)
                if (response != null) _tratativaSelecionada.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _carregando.value = false
            }
        }
    }
}
