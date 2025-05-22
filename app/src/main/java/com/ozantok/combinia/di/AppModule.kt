package com.ozantok.combinia.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozantok.combinia.data.repository.AuthRepositoryImpl
import com.ozantok.combinia.data.repository.OutfitRepositoryImpl
import com.ozantok.combinia.domain.repository.AuthRepository
import com.ozantok.combinia.domain.repository.OutfitRepository
import com.ozantok.combinia.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Firebase Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Firestore
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // Repositories
    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideOutfitRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): OutfitRepository = OutfitRepositoryImpl(firestore, firebaseAuth)

    // Auth UseCases
    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase =
        SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase =
        SignUpUseCase(repository)

    // Outfit UseCases
    @Provides
    @Singleton
    fun provideLikePostUseCase(repository: OutfitRepository): LikePostUseCase =
        LikePostUseCase(repository)

    @Provides
    @Singleton
    fun provideFavoritePostUseCase(repository: OutfitRepository): FavoritePostUseCase =
        FavoritePostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCommentsUseCase(repository: OutfitRepository): GetCommentsUseCase =
        GetCommentsUseCase(repository)

    @Provides
    @Singleton
    fun provideAddCommentUseCase(repository: OutfitRepository): AddCommentUseCase =
        AddCommentUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLikeCountUseCase(repository: OutfitRepository): GetLikeCountUseCase =
        GetLikeCountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCommentCountUseCase(repository: OutfitRepository): GetCommentCountUseCase =
        GetCommentCountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetOutfitPostsUseCase(repository: OutfitRepository): GetOutfitPostsUseCase =
        GetOutfitPostsUseCase(repository)
}
