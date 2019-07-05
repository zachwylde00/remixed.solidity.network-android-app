package com.kyberswap.android.presentation.main.kybercode


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kyberswap.android.AppExecutors
import com.kyberswap.android.R
import com.kyberswap.android.databinding.FragmentKyberCodeBinding
import com.kyberswap.android.domain.SchedulerProvider
import com.kyberswap.android.presentation.base.BaseFragment
import com.kyberswap.android.presentation.helper.Navigator
import com.kyberswap.android.presentation.listener.addTextChangeListener
import com.kyberswap.android.util.di.ViewModelFactory
import javax.inject.Inject


class KyberCodeFragment : BaseFragment() {

    private lateinit var binding: FragmentKyberCodeBinding

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var appExecutors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider
    private var fromLandingPage: Boolean? = null


    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(KyberCodeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromLandingPage = arguments?.getBoolean(FROM_LANDING_PAGE_EXTRA, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKyberCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.imgBack.setOnClickListener {
            activity?.onBackPressed()



        binding.edtKyberCode.addTextChangeListener {
            onTextChanged { s, _, _, _ ->
                val count = s.toString().trim().split(" ").size
                binding.tvApply.isEnabled = count > 0
    


        binding.tvApply.setOnClickListener {
            viewModel.createWalletByKyberCode(
                binding.edtKyberCode.text?.trim().toString(),
                if (binding.edtWalletName.text.isNotEmpty()) binding.edtWalletName.text.toString()
                else getString(R.string.default_wallet_name)
            )


        viewModel.getKyberCodeCallback.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { state ->
                showProgress(state == KyberCodeState.Loading)
                when (state) {
                    is KyberCodeState.Success -> {
                        onKyberCodeFinish()
            
                    is KyberCodeState.ShowError -> {
                        showAlert(state.message ?: getString(R.string.something_wrong))
            
        
    
)

    }

    private fun onKyberCodeFinish() {
        if (fromLandingPage == true) {
            navigator.navigateToHome()
 else {
            activity?.onBackPressed()

    }


    companion object {
        private const val FROM_LANDING_PAGE_EXTRA = "from_landing_page_extra"
        fun newInstance(fromLandingPage: Boolean = false) = KyberCodeFragment()
            .apply {
                arguments = Bundle().apply {
                    putBoolean(FROM_LANDING_PAGE_EXTRA, fromLandingPage)
        
    
    }


}
