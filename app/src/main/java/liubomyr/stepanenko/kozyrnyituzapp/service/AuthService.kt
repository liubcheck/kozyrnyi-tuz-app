package liubomyr.stepanenko.kozyrnyituzapp.service

import liubomyr.stepanenko.kozyrnyituzapp.model.AuthRequest
import liubomyr.stepanenko.kozyrnyituzapp.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/authenticate")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}