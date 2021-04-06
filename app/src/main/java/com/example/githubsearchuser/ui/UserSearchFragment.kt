package com.example.githubsearchuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearchuser.R
import com.example.githubsearchuser.data.ResponseResult
import com.example.githubsearchuser.data.UserItem
import com.example.githubsearchuser.databinding.UserListFragmentBinding
import com.example.githubsearchuser.ui.adapter.UserListAdapter
import com.example.githubsearchuser.viewmodel.UserSearchViewModel
import com.example.githubsearchuser.widget.ItemClick
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * User List
 * - input username and search
 * - click user goto user's info fragment
 */
class UserSearchFragment : Fragment() {

    private lateinit var viewBinding: UserListFragmentBinding
    private val userSearchViewModel: UserSearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(
            inflater, R.layout.user_list_fragment, container, false
        )
        viewBinding.lifecycleOwner = this
        initView()
        return viewBinding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenResumed {
            viewBinding.inputUserName.afterTextChangedFlow().debounce(1000)
                .collect {
                    if (it.toString().isNotEmpty()) {
                        userSearchViewModel.setSearchUserName(it.toString())
                        userSearchViewModel.clearDataSource()
                        getView()?.hideKeyboard()
                    }
                }
        }
    }

    private fun initView() {
        val adapter = UserListAdapter(object : ItemClick<UserItem> {
            override fun onClicked(view: View, userListItem: UserItem) {
                //跳轉至userinfo頁面
                val userList2UserInfo =
                    UserSearchFragmentDirections.actionUserListFragmentToUserInfoFragment(
                        userListItem.login
                    )
                Navigation.findNavController(view)
                    .navigate(userList2UserInfo)
            }
        })

        viewBinding.userList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    viewBinding.userList.context,
                    (viewBinding.userList.layoutManager as LinearLayoutManager).orientation
                )
            )
            this.adapter = adapter
        }

        userSearchViewModel.pagedList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        userSearchViewModel.getState().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ResponseResult.Success,
                is ResponseResult.Complete ->
                    viewBinding.progressBar.visibility = View.GONE
                is ResponseResult.Failure -> {
                    viewBinding.progressBar.visibility = View.GONE
                    state.catchException?.let { showToast(it) }
                }
                is ResponseResult.GenericError -> {
                    viewBinding.progressBar.visibility = View.GONE
                    showToast(state.catchException)
                }
                is ResponseResult.Pending ->
                    viewBinding.progressBar.visibility = View.VISIBLE
            }
        })

        getNetworkLiveData().observe(viewLifecycleOwner, Observer { available ->
            if (!available) {
                Toast.makeText(activity, R.string.network_not_available, Toast.LENGTH_SHORT).show()
            }
        })
    }
}