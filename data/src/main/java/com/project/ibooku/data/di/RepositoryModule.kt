package com.project.ibooku.data.di

import com.project.ibooku.data.repository.LibraryRepositoryImpl
import com.project.ibooku.data.remote.service.external.CentralService
import com.project.ibooku.data.remote.service.external.NaruService
import com.project.ibooku.data.remote.service.general.BookService
import com.project.ibooku.data.remote.service.general.ReviewService
import com.project.ibooku.data.remote.service.general.TMapService
import com.project.ibooku.data.remote.service.general.UserService
import com.project.ibooku.data.repository.BookRepositoryImpl
import com.project.ibooku.data.repository.MapRepositoryImpl
import com.project.ibooku.data.repository.ReviewRepositoryImpl
import com.project.ibooku.data.repository.UserRepositoryImpl
import com.project.ibooku.domain.respository.BookRepository
import com.project.ibooku.domain.respository.LibraryRepository
import com.project.ibooku.domain.respository.MapRepository
import com.project.ibooku.domain.respository.ReviewRepository
import com.project.ibooku.domain.respository.UserRepository
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
    ): LibraryRepository =
        LibraryRepositoryImpl(naruService, centralService)

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository =
        UserRepositoryImpl(userService = userService)

    @Provides
    @Singleton
    fun provideReviewRepository(reviewService: ReviewService): ReviewRepository =
        ReviewRepositoryImpl(reviewService = reviewService)

    @Provides
    @Singleton
    fun provideBookRepository(bookService: BookService): BookRepository =
        BookRepositoryImpl(bookService = bookService)

    @Provides
    @Singleton
    fun provideMapRepository(tMapService: TMapService): MapRepository =
        MapRepositoryImpl(tMapService = tMapService)
}