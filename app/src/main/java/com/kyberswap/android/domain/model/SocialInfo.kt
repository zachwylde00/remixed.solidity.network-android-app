package com.kyberswap.android.domain.model

import android.os.Parcelable
import com.kyberswap.android.presentation.main.profile.LoginType
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SocialInfo(
    val type: LoginType,
    val displayName: String? = null,
    val accessToken: String? = null,
    val photoUrl: String? = null,
    val email: String? = null,
    val subscription: Boolean = false,
    val twoFa: String? = null,
    val oAuthToken: String? = null,
    val oAuthTokenSecret: String? = null
) : Parcelable