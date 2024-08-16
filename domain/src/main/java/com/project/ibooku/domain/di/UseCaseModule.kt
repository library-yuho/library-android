package com.project.ibooku.domain.di

import com.project.ibooku.domain.respository.LibraryRepository
import com.project.ibooku.domain.usecase.PopularBooksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providePopularBooksUseCase(
        libraryRepository: LibraryRepository
    ): PopularBooksUseCase {
        return PopularBooksUseCase(libraryRepository)
    }
}