package com.project.ibooku.domain.usecase.user

import com.project.ibooku.domain.respository.UserRepository
import javax.inject.Inject

class CheckEmailExistUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String) =
        userRepository.checkEmailExist(email)
}