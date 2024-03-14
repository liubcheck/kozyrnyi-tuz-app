package liubomyr.stepanenko.kozyrnyituzapp.screens.barbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import liubomyr.stepanenko.kozyrnyituzapp.model.Barber
import liubomyr.stepanenko.kozyrnyituzapp.service.BarberService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import liubomyr.stepanenko.kozyrnyituzapp.utils.AuthInterceptor
import liubomyr.stepanenko.kozyrnyituzapp.utils.Constants
import liubomyr.stepanenko.kozyrnyituzapp.utils.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BarbersViewModel : ViewModel() {
    private val _barbers = MutableLiveData<List<Barber>>()
    val barbers: LiveData<List<Barber>> = _barbers

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val barberService: BarberService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
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