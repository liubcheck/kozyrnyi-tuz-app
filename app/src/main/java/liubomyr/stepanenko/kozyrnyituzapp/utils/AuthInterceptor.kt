package liubomyr.stepanenko.kozyrnyituzapp.utils

import kotlinx.coroutines.runBlocking
import liubomyr.stepanenko.kozyrnyituzapp.model.AuthRequest
import liubomyr.stepanenko.kozyrnyituzapp.service.AuthService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = SharedPreferencesManager.getString("token", "")

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        val processed = chain.proceed(newRequest)

        if (processed.code == 401) {
            val email = SharedPreferencesManager.getString("email", "")
            val password = SharedPreferencesManager.getString("password", "")

            val authResponse = runBlocking { authService.login(AuthRequest.from(email, password)) }
            return if (authResponse.isSuccessful) {
                val newToken = authResponse.body()!!.token
                SharedPreferencesManager.saveString("token", newToken)
                val newAuthRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()

                processed.close()
                chain.proceed(newAuthRequest)
            } else {
                throw Exception("Failed to refresh token")
            }
        }
        else {
            return processed
        }
    }

    private val okHttpClient = OkHttpClient()

    private val authService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)
    }
}
