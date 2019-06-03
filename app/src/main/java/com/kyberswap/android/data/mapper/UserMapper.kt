package com.kyberswap.android.data.mapper

import com.kyberswap.android.data.api.user.LoginUserEntity
import com.kyberswap.android.data.api.user.UserStatusEnity
import com.kyberswap.android.domain.model.LoginUser
import com.kyberswap.android.domain.model.UserStatus
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun transform(entity: UserStatusEnity): UserStatus {
        return UserStatus(entity)
    }

    fun transform(entity: LoginUserEntity): LoginUser {
        return LoginUser(entity)
    }
}