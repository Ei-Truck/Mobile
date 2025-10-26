package com.example.eitruck.data.remote.network.service.postgres

import com.example.eitruck.model.DriverMonthlyReport
import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.Region
import com.example.eitruck.model.Segments
import com.example.eitruck.model.Travel
import com.example.eitruck.model.Units
import com.example.eitruck.model.User
import com.example.eitruck.model.UserPassword
import com.example.eitruck.model.WeeklyReport
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface AuthService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface UserService {
    @GET("/usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("/usuarios/telefone/{telefone}")
    suspend fun getUserByPhone(@Path("telefone") telefone: String): User

    @PATCH("/usuarios/senha/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserPassword): User

    @Multipart
    @POST("/usuarios/{id}/foto")
    suspend fun uploadPhoto(@Path("id") id: Int, @Part photo: MultipartBody.Part): User
}

interface TravelService {
    @GET("/viagens/relatorio-simples")
    suspend fun getTravels(): List<Travel>
}

interface InfractionService {

    @GET("/infracoes/relatorio")
    suspend fun getInfractions(): List<WeeklyReport>

}

interface SegmentsService {

    @GET("/segmentos")
    suspend fun getSegments(): List<Segments>
}

interface UnitsService {

    @GET("/unidades")
    suspend fun getUnits(): List<Units>
}

interface RegionService {

    @GET("/localidades")
    suspend fun getRegions() : List<Region>
}

interface DriverService {

    @GET("/motoristas/pontuacao-mensal")
    suspend fun getDriverMonthlyReport(): List<DriverMonthlyReport>
}