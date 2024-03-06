package liubomyr.stepanenko.kozyrnyituzapp.service

import liubomyr.stepanenko.kozyrnyituzapp.model.Barbershop
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BarbershopService {
    @GET("/barbershop")
    suspend fun getAllBarbershops(): Response<List<Barbershop>>

    @GET("/barbershop/{barbershopId}")
    suspend fun getBarbershopById(@Path("barbershopId") barbershopId: Long): Response<Barbershop>
}
