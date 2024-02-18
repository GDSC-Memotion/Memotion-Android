package konkuk.gdsc.memotion.data.api

import konkuk.gdsc.memotion.data.model.request.RequestPostDiary
import konkuk.gdsc.memotion.data.model.request.RequestPutDiary
import konkuk.gdsc.memotion.data.model.response.ResponseDeleteDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDailyDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDetailDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetMonthlyDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePostDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePutDiary
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface DiaryApi {
    @Multipart
    @POST("diary")
    suspend fun postDiary(
        @Part
        request: RequestPostDiary,
        @Part
        imageUris: List<MultipartBody.Part?>
    ): ResponsePostDiary

    @Multipart
    @PUT("diary/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId")
        diaryId: Long,
        @Part
        request: RequestPutDiary,
        @Part
        imageUris: List<MultipartBody.Part?>,
    ): ResponsePutDiary

    @DELETE("diary/{diaryId}")
    suspend fun deleteDiary(
        @Path("diaryId")
        diaryId: Long,
    ): ResponseDeleteDiary

    @GET("diary")
    suspend fun getDailyDiary(
        @Query("period")
        period: String, // 240122 형식
    ): ResponseGetDailyDiary

    @GET("diary/calendar")
    suspend fun getMonthlyDiary(
        @Query("period")
        period: String, // 2401 형식
    ): ResponseGetMonthlyDiary

    @GET("diary/{diaryId}")
    suspend fun getDetailDiary(
        @Path("diaryId")
        diaryId: Long,
    ): ResponseGetDetailDiary
}