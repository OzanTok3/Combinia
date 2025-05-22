package com.ozantok.combinia.di

import com.google.firebase.auth.FirebaseAuth
import com.ozantok.combinia.data.repository.AuthRepositoryImpl
import com.ozantok.combinia.domain.repository.AuthRepository
import com.ozantok.combinia.domain.usecase.SignInUseCase
import com.ozantok.combinia.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AuthRepository): SignInUseCase =
        SignInUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AuthRepository): SignUpUseCase =
        SignUpUseCase(repository)
}
