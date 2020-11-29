package com.example.userenrollmentapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userenrollmentapp.databinding.FragmentUserListBinding

class UserListFragment : Fragment(), UserListAdapter.OnClick {

    // Binding
    private lateinit var binding: FragmentUserListBinding

    // ViewModel
    private lateinit var viewModel: UserListViewModel
    private val userListAdapter by lazy { UserListAdapter(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_list,container,false)
        viewModel = ViewModelProvider(requireActivity()).get(UserListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpRecyclerView()
        viewModel.getUsers()
        setUpObservers()

    }

    private fun setUpObservers() {
        viewModel.usersList.observe(viewLifecycleOwner, {
            if (it != null) {
                userListAdapter.submitList(it)
                Log.d("TAG", "setUpObservers: usersListChanged:$it")
            }
        })
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            adapter = this@UserListFragment.userListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        Log.d("TAG", "setUpRecyclerView:  "+viewModel.usersList.value)
    }

    override fun userDeleteClickListener(userId: String) {
        viewModel.deleteUser(userId)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: UserListFragment ")
//        viewModel.getUsers()
        userListAdapter.notifyDataSetChanged()
    }
}