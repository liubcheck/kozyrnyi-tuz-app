package liubomyr.stepanenko.kozyrnyituzapp.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.service.BarberService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import liubomyr.stepanenko.kozyrnyituzapp.service.BarbershopService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BarbersViewModel : ViewModel() {
    private val _barbers = MutableLiveData<List<Barber>>()
    val barbers: LiveData<List<Barber>> = _barbers

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

    private val barberService: BarberService by lazy {
        Retrofit.Builder()
            .baseUrl("https://11f1-89-70-36-197.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(BarberService::class.java)
    }

    init {
        loadBarbers()
    }

    fun loadBarbers() {
        viewModelScope.launch {
            val response = barberService.getAllBarbers()
            if (response.isSuccessful) {
                _barbers.postValue(response.body())
            }
        }
    }
}