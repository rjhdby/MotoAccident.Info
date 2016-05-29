package info.motoaccident.network

import info.motoaccident.network.modeles.auth.AuthResultModel
import info.motoaccident.network.modeles.list.ListResultModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import rx.schedulers.Schedulers

interface HttpRequestService {

    @GET("request.php?m=list")
    fun list(): Observable<ListResultModel>

    @GET("request.php?m=auth")
    fun auth(@Query("l") login: String, @Query("p") passHash: String): Observable<AuthResultModel>

    @GET("request.php?m=gcm")
    fun gcm(@Query("id") id: Int, @Query("k") key: String, @Query("i") imei: String)

    companion object {
        val URL = "http://apk.moto.msk.ru/"
        val logInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logInterceptor).build()
        val api = Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build()
                .create(HttpRequestService::class.java)
    }
}
