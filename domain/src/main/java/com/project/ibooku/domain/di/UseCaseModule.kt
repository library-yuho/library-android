package com.project.ibooku.domain.di

import com.project.ibooku.domain.respository.BookRepository
import com.project.ibooku.domain.respository.LibraryRepository
import com.project.ibooku.domain.respository.ReviewRepository
import com.project.ibooku.domain.respository.UserRepository
import com.project.ibooku.domain.usecase.book.GetBookInfoUseCase
import com.project.ibooku.domain.usecase.book.GetBookSearchResultListUseCase
import com.project.ibooku.domain.usecase.external.KeywordSearchResultUseCase
import com.project.ibooku.domain.usecase.external.PopularBooksUseCase
import com.project.ibooku.domain.usecase.review.GetReviewListUseCase
import com.project.ibooku.domain.usecase.review.WriteReviewUseCase
import com.project.ibooku.domain.usecase.user.LoginUseCase
import com.project.ibooku.domain.usecase.user.SignUpUseCase
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
    fun providePopularBooksUseCase(libraryRepository: LibraryRepository) =
        PopularBooksUseCase(libraryRepository)

    @Provides
    @Singleton
    fun provideKeywordSearchResultUseCase(libraryRepository: LibraryRepository) =
        KeywordSearchResultUseCase(libraryRepository)

    @Provides
    @Singleton
    fun provideLoginUseCase(userRepository: UserRepository) =
        LoginUseCase(userRepository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(userRepository: UserRepository) =
        SignUpUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetReviewListUseCase(reviewRepository: ReviewRepository) =
        GetReviewListUseCase(reviewRepository)

    @Provides
    @Singleton
    fun provideWriteReviewUseCase(reviewRepository: ReviewRepository) =
        WriteReviewUseCase(reviewRepository)

    @Provides
    @Singleton
    fun provideGetBookInfoUseCase(bookRepository: BookRepository) =
        GetBookInfoUseCase(bookRepository)

    @Provides
    @Singleton
    fun provideGetBookSearchResultListUseCase(bookRepository: BookRepository) =
        GetBookSearchResultListUseCase(bookRepository)
}