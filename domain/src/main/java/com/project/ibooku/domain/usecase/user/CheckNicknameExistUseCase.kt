package com.project.ibooku.domain.usecase.user

import com.project.ibooku.domain.respository.UserRepository
import javax.inject.Inject

class CheckNicknameExistUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String) =
        userRepository.checkNicknameExist(nickname = nickname)
}