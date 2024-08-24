package com.project.ibooku.domain.usecase.user

import com.project.ibooku.domain.respository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        gender: String,
        birth: String,
        nickname: String
    ) =
        userRepository.signUp(
            email = email,
            password = password,
            gender = gender,
            birth = birth,
            nickname = nickname,
        )
}