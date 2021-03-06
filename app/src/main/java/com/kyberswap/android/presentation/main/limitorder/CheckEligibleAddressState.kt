package com.kyberswap.android.presentation.main.limitorder

import com.kyberswap.android.domain.model.EligibleAddress

sealed class CheckEligibleAddressState {
    object Loading : CheckEligibleAddressState()
    class ShowError(val message: String?, val isNetworkUnavailable: Boolean = false) :
        CheckEligibleAddressState()

    class Success(val eligibleAddress: EligibleAddress, val isWalletChangeEvent: Boolean = false) :
        CheckEligibleAddressState()
}
