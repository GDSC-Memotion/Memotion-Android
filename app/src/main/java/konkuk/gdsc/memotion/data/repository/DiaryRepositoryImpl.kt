package konkuk.gdsc.memotion.data.repository

import konkuk.gdsc.memotion.data.api.DiaryApi
import konkuk.gdsc.memotion.data.model.response.ResponseDeleteDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDailyDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetDetailDiary
import konkuk.gdsc.memotion.data.model.response.ResponseGetMonthlyDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePostDiary
import konkuk.gdsc.memotion.data.model.response.ResponsePutDiary
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWithImage
import konkuk.gdsc.memotion.domain.entity.diary.DiaryWithoutImage
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryApi: DiaryApi
) : DiaryRepository {
    override suspend fun getDailyDiary(date: String): Result<ResponseGetDailyDiary> {
        return runCatching {
            diaryApi.getDailyDiary(date)
        }
    }

    override suspend fun getMonthlyDiary(period: String): Result<ResponseGetMonthlyDiary> {
        return runCatching {
            diaryApi.getMonthlyDiary(period)
        }
    }

    override suspend fun getDetailDiary(diaryId: Long): Result<ResponseGetDetailDiary> {
        return runCatching {
            diaryApi.getDetailDiary(diaryId)
        }
    }

    override suspend fun postDiary(
        diary: DiaryWithoutImage,
        images: List<MultipartBody.Part>
    ): Result<ResponsePostDiary> {
        return runCatching {
            val diary = diary.asRequestPostDiary()
            diaryApi.postDiary(diary, images)
        }
    }

    override suspend fun putDiary(
        diaryId: Long,
        diary: DiaryWithImage,
        newImages: List<MultipartBody.Part>
    ): Result<ResponsePutDiary> {
        return runCatching {
            val diary = diary.asRequestPutDiary()
            diaryApi.editDiary(diaryId, diary, newImages)
        }
    }

    override suspend fun deleteDiary(diaryId: Long): Result<ResponseDeleteDiary> {
        return runCatching {
            diaryApi.deleteDiary(diaryId)
        }
    }


}