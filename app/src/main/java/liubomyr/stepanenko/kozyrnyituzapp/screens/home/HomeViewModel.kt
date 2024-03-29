package liubomyr.stepanenko.kozyrnyituzapp.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop
import liubomyr.stepanenko.kozyrnyituzapp.service.BarbershopService
import liubomyr.stepanenko.kozyrnyituzapp.utils.AuthInterceptor
import liubomyr.stepanenko.kozyrnyituzapp.utils.Constants
import liubomyr.stepanenko.kozyrnyituzapp.utils.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel : ViewModel() {
    private val _barbershops = MutableLiveData<List<Barbershop>>()
    val barbershops: LiveData<List<Barbershop>> = _barbershops
    private val _selectedBarbershop = MutableLiveData<Barbershop?>()
    val selectedBarbershop: LiveData<Barbershop?> = _selectedBarbershop

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val barbershopService: BarbershopService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BarbershopService::class.java)
    }

    init {
        loadBarbershops()
    }

    private fun loadBarbershops() {
        viewModelScope.launch {
            val response = barbershopService.getAllBarbershops()
            if (response.isSuccessful) {
                _barbershops.postValue(response.body())
            }
        }
    }

    fun loadBarbershopById(barbershopId: Long) {
        viewModelScope.launch {
            val response = barbershopService.getBarbershopById(barbershopId)
            if (response.isSuccessful) {
                _selectedBarbershop.postValue(response.body())
            } else {
                // Handle error or set _selectedBarbershop to null
                _selectedBarbershop.postValue(null)
            }
        }
    }
}
