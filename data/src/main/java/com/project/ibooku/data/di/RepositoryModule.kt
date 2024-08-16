package com.project.ibooku.data.di

import com.project.ibooku.data.repository.LibraryRepositoryImpl
import com.project.ibooku.data.remote.service.CentralService
import com.project.ibooku.data.remote.service.NaruService
import com.project.ibooku.domain.respository.LibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLibraryRepository(
        naruService: NaruService,
        centralService: CentralService
    ): LibraryRepository {
        return LibraryRepositoryImpl(naruService, centralService)
    }
}