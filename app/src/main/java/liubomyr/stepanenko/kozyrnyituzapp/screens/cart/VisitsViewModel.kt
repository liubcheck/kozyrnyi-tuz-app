package liubomyr.stepanenko.kozyrnyituzapp.screens.cart

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import liubomyr.stepanenko.kozyrnyituzapp.model.AddVisitRequest
import liubomyr.stepanenko.kozyrnyituzapp.model.Visit
import liubomyr.stepanenko.kozyrnyituzapp.service.VisitService
import liubomyr.stepanenko.kozyrnyituzapp.utils.AuthInterceptor
import liubomyr.stepanenko.kozyrnyituzapp.utils.Constants
import liubomyr.stepanenko.kozyrnyituzapp.utils.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VisitsViewModel : ViewModel() {
    private val id = 0L;
    private val _visitConfirmation = MutableLiveData<Visit>()
    val visitConfirmation: LiveData<Visit> = _visitConfirmation
    private val _visits = MutableLiveData<List<Visit>>()
    val visits: LiveData<List<Visit>> = _visits

    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
            .registerTypeAdapter(LocalDateTime::class.java, localDateTimeSerializer)
            .create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    private val visitService: VisitService by lazy {
        retrofit.create(VisitService::class.java)
    }

    // Gson adapters
    private val localDateTimeDeserializer = JsonDeserializer { json, _, _ ->
        LocalDateTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    private val localDateTimeSerializer = JsonSerializer<LocalDateTime> { src, _, _ ->
        JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    fun confirmVisit(addVisitRequest: AddVisitRequest, context: Context,
                     onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = visitService.addVisit(addVisitRequest)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Visit confirmed", Toast.LENGTH_SHORT).show()
                        onSuccess() // Execute the success callback
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("Failed to confirm visit") // Execute the error callback
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error confirming visit") // Execute the error callback
                }
            }
        }
    }

    private fun loadVisits() {
        viewModelScope.launch {
            val response = visitService.getAllVisits()
            if (response.isSuccessful) {
                _visits.postValue(response.body())
            }
        }
    }
}
