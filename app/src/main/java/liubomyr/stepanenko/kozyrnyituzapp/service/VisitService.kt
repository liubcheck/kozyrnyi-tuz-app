package liubomyr.stepanenko.kozyrnyituzapp.service

import liubomyr.stepanenko.kozyrnyituzapp.model.AddVisitRequest
import liubomyr.stepanenko.kozyrnyituzapp.model.Visit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VisitService {
    @POST("/visit/register")
    suspend fun addVisit(@Body addVisitRequest: AddVisitRequest): Response<Visit>

    @GET("/visit")
    suspend fun getAllVisits(): Response<List<Visit>>
}
