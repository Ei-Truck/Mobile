package com.example.eitruck.data.remote.network.client.postgres

import com.example.eitruck.data.remote.network.service.postgres.AuthService
import com.example.eitruck.data.remote.network.service.postgres.DashService
import com.example.eitruck.data.remote.network.service.postgres.DriverService
import com.example.eitruck.data.remote.network.service.postgres.TravelService
import com.example.eitruck.data.remote.network.service.postgres.UserService
import com.example.eitruck.data.remote.network.service.postgres.InfractionService
import com.example.eitruck.data.remote.network.service.postgres.RecordService
import com.example.eitruck.data.remote.network.service.postgres.RegionService
import com.example.eitruck.data.remote.network.service.postgres.SegmentsService
import com.example.eitruck.data.remote.network.service.postgres.UnitsService
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostgresClient(private val token: String?) {

    private val baseUrl = "https://api-sql-qa.onrender.com"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
        .build()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy { retrofit.create(AuthService::class.java) }
    val userService: UserService by lazy { retrofit.create(UserService::class.java) }
    val travelService: TravelService by lazy { retrofit.create(TravelService::class.java) }
    val infractionsService: InfractionService by lazy { retrofit.create(InfractionService::class.java) }
    val dashService: DashService by lazy { retrofit.create(DashService::class.java) }
    val segmentsService : SegmentsService by lazy { retrofit.create(SegmentsService::class.java) }
    val unitsService : UnitsService by lazy { retrofit.create(UnitsService::class.java) }
    val regionService : RegionService by lazy { retrofit.create(RegionService::class.java) }
    val recordService : RecordService by lazy { retrofit.create(RecordService::class.java) }
    val driverService : DriverService by lazy { retrofit.create(DriverService::class.java) }
}
