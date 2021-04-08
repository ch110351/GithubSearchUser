package com.example.githubsearchuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.githubsearchuser.R
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.databinding.UserInfoFragmentBinding
import com.example.githubsearchuser.viewmodel.UserInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * User Info
 * -show user's info
 */
class UserInfoFragment : Fragment() {

    private lateinit var viewBinding: UserInfoFragmentBinding
    private val userInfoViewModel: UserInfoViewModel by viewModel()
    private val args: UserInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(
            inflater, R.layout.user_info_fragment, container, false
        )
        viewBinding.lifecycleOwner = this
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            userInfoViewModel.getUsers(args.userLogin).observe(viewLifecycleOwner,
                Observer { result ->
                    when (result) {
                        is ResponseResult.Success -> {
                            viewBinding.progressBar.visibility = View.GONE
                            Log.d("Wesley", result.toString())
                            result.let { userinfo ->
                                viewBinding.user = userinfo.data
                            }
                        }
                        is ResponseResult.GenericError -> {
                            showToast(result.catchException)
                        }
                        is ResponseResult.Failure -> {
                            result.catchException?.let { showToast(it) }
                        }
                        is ResponseResult.Pending -> {
                            viewBinding.progressBar.visibility = View.VISIBLE
                        }
                        is ResponseResult.Complete -> {
                            viewBinding.progressBar.visibility = View.GONE
                        }
                    }
                })
        }


    }
}