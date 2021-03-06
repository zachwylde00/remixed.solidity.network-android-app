package com.kyberswap.android.util

import com.kyberswap.android.domain.model.Token
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import javax.inject.Inject

class SwapClient @Inject constructor(private val web3j: Web3j) {

    private fun balanceOf(owner: String): Function {
        return Function(
            "balanceOf",
            listOf(Address(owner)),
            listOf<TypeReference<*>>(object : TypeReference<Uint256>() {
            })
        )
    }

    private fun getExpectedRate(
        srcToken: String,
        destToken: String,
        srcTokenAmount: String
    ): Function {
        return Function(
            "getExpectedRate",
            listOf(
                Address(srcToken),
                Address(destToken),
                Uint256(BigInteger(srcTokenAmount))
            ),
            listOf<TypeReference<*>>(
                object : TypeReference<Uint256>() {
                },
                object : TypeReference<Uint256>() {

                })
        )
    }

    @Throws(Exception::class)
    fun getEthBalance(owner: String): BigInteger {
        return web3j
            .ethGetBalance(
                owner,
                DefaultBlockParameterName.LATEST
            )
            .send()
            .balance
    }

    @Throws(Exception::class)
    private fun callSmartContractFunction(
        function: Function,
        contractAddress: String,
        fromAddress: String
    ): String {
        val encodedFunction = FunctionEncoder.encode(function)
        val response = web3j.ethCall(
            Transaction.createEthCallTransaction(fromAddress, contractAddress, encodedFunction),
            DefaultBlockParameterName.LATEST
        )
            .send()

        return response.value
    }

    @Throws(Exception::class)
    fun getBalance(walletAddress: String, tokenAddress: String): BigDecimal? {
        val function = balanceOf(walletAddress)
        val responseValue = callSmartContractFunction(function, tokenAddress, walletAddress)

        val response = FunctionReturnDecoder.decode(
            responseValue, function.outputParameters
        )
        return if (response.size == 1) {
            BigDecimal((response[0] as Uint256).value)
        } else {
            null
        }
    }


    @Throws(Exception::class)
    fun getExpectedRate(
        walletAddress: String,
        tokenAddress: String,
        srcToken: String,
        destToken: String,
        srcTokenAmount: String
    ): List<Uint256> {
        val function = getExpectedRate(srcToken, destToken, srcTokenAmount)
        val responseValue = callSmartContractFunction(function, tokenAddress, walletAddress)

        val responses = FunctionReturnDecoder.decode(
            responseValue, function.outputParameters
        )
        val rateResult = ArrayList<Uint256>()
        for (rates in responses) {
            val rate = rates as Uint256
            rateResult.add(rate)
        }
        return rateResult
    }

    @Throws(Exception::class)
    fun getBalance(owner: String, token: Token): Token {
        return token.updateBalance(
            if (token.isETH) {
                Convert.fromWei(BigDecimal(getEthBalance(owner)), Convert.Unit.ETHER)
            } else {
                Convert.fromWei(
                    getBalance(owner, token.tokenAddress) ?: BigDecimal.ZERO,
                    Convert.Unit.ETHER
                )
            }
        )
    }
}
