package konkuk.gdsc.memotion.domain.repository

import konkuk.gdsc.memotion.data.model.response.ResponseDeleteDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDailyDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDetailDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetMonthlyDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePostDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePutDiary
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWithImage
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWithoutImage
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWriting
import okhttp3.MultipartBody
import retrofit2.http.Multipart

interface DiaryRepository {
    suspend fun getDailyDiary(
        date: String,
    ): Result<ResponseGetDailyDiary>

    suspend fun getMonthlyDiary(
        period: String,
    ): Result<ResponseGetMonthlyDiary>

    suspend fun getDetailDiary(
        diaryId: Long,
    ): Result<ResponseGetDetailDiary>

    suspend fun postDiary(
        diary: DiaryWithoutImage,
        images: List<MultipartBody.Part>,
    ): Result<ResponsePostDiary>

    // diary 수정
    suspend fun putDiary(
        diaryId: Long,
        diary: DiaryWithImage,
        newImages: List<MultipartBody.Part>,
    ): Result<ResponsePutDiary>

    suspend fun deleteDiary(
        diaryId: Long,
    ): Result<ResponseDeleteDiary>
}