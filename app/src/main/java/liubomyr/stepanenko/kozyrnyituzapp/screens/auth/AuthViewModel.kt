package liubomyr.stepanenko.kozyrnyituzapp.screens.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import liubomyr.stepanenko.kozyrnyituzapp.model.AuthRequest
import liubomyr.stepanenko.kozyrnyituzapp.service.AuthService
import liubomyr.stepanenko.kozyrnyituzapp.utils.Constants
import liubomyr.stepanenko.kozyrnyituzapp.utils.SharedPreferencesManager
import liubomyr.stepanenko.kozyrnyituzapp.utils.ToastManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class AuthState {
    data object HasAuth : AuthState()
    data object NoAuth : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _state = MutableLiveData<AuthState?>()
    val state: LiveData<AuthState?> = _state

    private val okHttpClient = OkHttpClient.Builder().build()

    private val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)
    }

    init {
        val email = SharedPreferencesManager.getString("email", "")
        val password = SharedPreferencesManager.getString("password", "")

        if (email.isNotEmpty() && password.isNotEmpty()) {
            _state.value = AuthState.HasAuth
        } else {
            _state.value = AuthState.NoAuth
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = null
            delay(1000)
            val response = authService.login(AuthRequest.from(email, password))
            Log.i("AuthViewModel", "login: $response")

            if (response.isSuccessful) {
                SharedPreferencesManager.saveString("email", email)
                SharedPreferencesManager.saveString("password", password)
                SharedPreferencesManager.saveString("token", response.body()!!.token)
                _state.value = AuthState.HasAuth
            } else {
                _state.value = AuthState.NoAuth
                ToastManager.showToast("Login failed")
            }
        }
    }
}