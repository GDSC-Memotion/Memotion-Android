package konkuk.gdsc.memotion.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import konkuk.gdsc.memotion.data.repository.DiaryRepositoryImpl
import konkuk.gdsc.memotion.domain.repository.DiaryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsDiaryRepository(
        diaryRepositoryImpl: DiaryRepositoryImpl
    ): DiaryRepository

    /*@Singleton
    @Binds
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindsPlantRepository(
        emotionRepositoryImpl: EmotionRepositoryImpl
    ): EmotionRepository*/
}