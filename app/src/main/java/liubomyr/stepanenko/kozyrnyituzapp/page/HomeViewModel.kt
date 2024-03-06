package liubomyr.stepanenko.kozyrnyituzapp.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop
import liubomyr.stepanenko.kozyrnyituzapp.service.BarbershopService
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
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkB0dXouY29tIiwiaWF0IjoxNzA5NjgyMTUxLCJleHAiOjE3MDk2ODU3NTF9.NQ_Ve_GYJP8itFcr_Ylr6R_7Qhy7RV-fTZp5JYftX8A")
                    .build()
                return chain.proceed(newRequest)
            }
        })
        .build()

    private val barbershopService: BarbershopService by lazy {
        Retrofit.Builder()
            .baseUrl("https://11f1-89-70-36-197.ngrok-free.app/")
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
