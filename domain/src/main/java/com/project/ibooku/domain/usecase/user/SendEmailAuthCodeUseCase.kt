package com.project.ibooku.domain.usecase.user

import com.project.ibooku.domain.respository.UserRepository
import javax.inject.Inject

class SendEmailAuthCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
   suspend operator fun invoke(email: String) =
       userRepository.sendEmailAuthCode(email = email)
}