package com.example.android.marsrealestate.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// 1. Base URL 설정
private const val BASE_URL = "https://mars.udacity.com/"

// 필터
enum class MarsApiFilter(val value: String) { SHOW_RENT("rent"), SHOW_BUY("buy"), SHOW_ALL("all") }

// 5. Moshi Builder와 KotlinJsonAdapterFactory를 사용하여 Moshi object를 생성하기
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// 2. Retorfit object 생성하기
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL) // 서버 엔드포인트의 경로 웹 주소를 지정하기 위해
        .build()

/**
 * 3. Retrofit이 HTTP 요청을 사용하여 웹 서버와 어떻게 통신하는지 방법을 정의하는 인터페이스 생성
 *
 * 서버와 통신하는 모든 메서드를 인터페이스로 구현하는 객체를 생성
 **/
interface MarsApiService {
    // retrofit은 내가 선언한 enum을 모르기 때문에 String type으로 선언해야 함
    @GET("realestate")
    suspend fun getProperties(@Query("filter") type: String): List<MarsProperty>
}

// 4. 공용 객체를 정의하여 Retrofit 서비스를 초기화
// ( 서비스 객체를 생성할 때 사용하는 표준 Kotlin 코드 패턴입니다. )
object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}