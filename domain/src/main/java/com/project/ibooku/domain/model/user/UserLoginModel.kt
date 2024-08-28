package com.project.ibooku.domain.model.user

data class UserLoginModel(
    val email: String?,
    val gender: String?,
    val birth: String?,
    val nickname: String?,
    val isSuccess: Boolean,
)