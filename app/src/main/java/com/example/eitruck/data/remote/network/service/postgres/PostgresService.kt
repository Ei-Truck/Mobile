package com.example.eitruck.data.remote.network.service.postgres

import com.example.eitruck.model.DashLegendaItem
import com.example.eitruck.model.DashOcorrenciaGravidade
import com.example.eitruck.model.DashOcorrenciaTotal
import com.example.eitruck.model.DashVariacaoInfracoes
import com.example.eitruck.model.DriverInfractions
import com.example.eitruck.model.DriverMonthlyReport
import com.example.eitruck.model.LoginRequest
import com.example.eitruck.model.LoginResponse
import com.example.eitruck.model.Region
import com.example.eitruck.model.Segments
import com.example.eitruck.model.Travel
import com.example.eitruck.model.Units
import com.example.eitruck.model.TravelAnalysisStatus
import com.example.eitruck.model.TravelAnalyzeRequest
import com.example.eitruck.model.TravelBasicVision
import com.example.eitruck.model.TravelDriverBasicVision
import com.example.eitruck.model.TravelDriverInfractions
import com.example.eitruck.model.TravelInfractionInfo
import com.example.eitruck.model.User
import com.example.eitruck.model.WeeklyReport
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface AuthService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}

interface UserService {
    @GET("/usuarios/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @Multipart
    @POST("/usuarios/{id}/foto")
    suspend fun uploadPhoto(@Path("id") id: Int, @Part photo: MultipartBody.Part): User
}

interface TravelService {
    @GET("/viagens/relatorio-simples")
    suspend fun getTravels(): List<Travel>

    @GET("/viagens/visao-basica/{id}")
    suspend fun getTravelsInfo(@Path("id") id: Int): TravelBasicVision

    @GET("/viagens/motorista-visao-basica/{id}")
    suspend fun getDriversInfo(@Path("id") id: Int): List<TravelDriverBasicVision>

    @GET("/viagens/motorista-infracoes/{id}")
    suspend fun getDriversInfractions(@Path("id") id: Int): List<TravelDriverInfractions>

    @GET("/viagens/quantidade-infracao-tipo-gravidade/{id}")
    suspend fun getInfractionsByGravity(@Path("id") id: Int): List<TravelInfractionInfo>

    @PATCH("/viagens/check-viagem/{id}")
    suspend fun updateTravelStatus(@Path("id") id: Int, @Body request: TravelAnalyzeRequest): List<TravelInfractionInfo>

    @GET("/viagens/{id}")
    suspend fun getTravelAnalysisStatus(@Path("id") id: Int): TravelAnalysisStatus
}

interface InfractionService {
    @GET("/infracoes/relatorio")
    suspend fun getInfractions(): List<WeeklyReport>

    @GET("/infracoes/variacao")
    suspend fun getInfractionsVariation(): List<DashVariacaoInfracoes>

    @GET("/infracoes/total-ocorrencias")
    suspend fun getInfractionsTotal(): List<DashOcorrenciaTotal>
}

interface DashService {
    @GET("/tipo-infracao/ocorrencia-tipo")
    suspend fun getInfractionsByType(): List<DashLegendaItem>

    @GET("/tipo-infracao/ocorrencia-gravidade")
    suspend fun getInfractionsByGravity(): List<DashOcorrenciaGravidade>
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

    @GET("/localidades/diff")
    suspend fun getRegions() : List<Region>
}

interface DriverService {

    @GET("/motoristas/pontuacao-mensal")
    suspend fun getDriverMonthlyReport(): List<DriverMonthlyReport>

    @GET("/motoristas/quantidade-infracoes")
    suspend fun getDriverInfractions(): List<DriverInfractions>
}