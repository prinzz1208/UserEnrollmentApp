package com.example.userenrollmentapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.userenrollmentapp.databinding.FragmentAddUserBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class AddUserFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: FragmentAddUserBinding
    private lateinit var viewModel: UserListViewModel
    private var UPLOAD_PIC = 4862
    private var profilePicUrl: Uri = "".toUri()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(UserListViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_user, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpAutoCompleteTextView()
        setUpObservers()
        setUpListeners()
    }

    private fun setUpObservers() {
        viewModel.currentUserImageUrl.observe(viewLifecycleOwner,{
            if(!it.isNullOrEmpty()){
                val user = User(
                        id = "",
                        firstName = binding.firstName.text.toString(),
                        lastName = binding.lastName.text.toString(),
                        gender = binding.gender.text.toString(),
                        dob = (binding.dateOfBirth.text.toString()),
                        homeTown = binding.homeTown.text.toString(),
                        state = binding.state.text.toString(),
                        country = binding.country.text.toString(),
                        phoneNumber = binding.phoneNumber.text.toString(),
                        telePhoneNumber = binding.telephoneNumber.text.toString(),
                        imageUrl = it.toString(),
                        dateOfCreation = null)
                viewModel.addUser(user)
                resetFields()
                Toast.makeText(requireActivity(), "User Added successfully", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "setUpListeners: UserAdded")
                (activity as MainActivity).viewPager.currentItem = 0
            }
        })
    }

    // LISTERNERS
    private fun setUpListeners() {

        // PHONE NUMBER TEXT FIELD
        binding.phoneNumber.addTextChangedListener {
            if(binding.phoneNumber.text.length == 10){
                closeKeyboard(requireContext(), binding.phoneNumber)
            }
        }

        // TELEPHONE NUMBER TEXT FIELD
        binding.telephoneNumber.addTextChangedListener {
            if(binding.telephoneNumber.text.length == 10){
                closeKeyboard(requireContext(), binding.phoneNumber)
            }
        }

        // DOB TEXT FIELD
        binding.dateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }
        // ADD USER BUTTON
        binding.addUserBtn.setOnClickListener {
            val gender = listOf("MALE","FEMALE","OTHER")
            if(checkBlanks()) {
                if( gender.contains(binding.gender.text.toString().toUpperCase())) {
                    if(binding.phoneNumber.text.toString().length == 10) {
                        val fireStorage = FirebaseStorage.getInstance().reference
                        val fileName = binding.firstName.text.toString() + "_" + (binding.dateOfBirth.text.toString())
                        viewModel.uploadImage(fireStorage, profilePicUrl, getFileExtension(profilePicUrl), fileName)

                    }else{
                        Toast.makeText(requireActivity(), "Please enter correct phone number", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireActivity(), "Please fill correct gender", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(), "Please fill all the details", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "setUpListeners: Validation Failed")

            }
        }

        // UPLOAD PROFILE PHOTO
        binding.profileImage.setOnClickListener {
            openFilePicker(UPLOAD_PIC)
        }
    }

    // UTILITY FUNCTIONS
    private fun checkBlanks(): Boolean {
        return !(binding.firstName.text.toString().isBlank()
                ||binding.lastName.text.toString().isBlank()
                ||binding.gender.text.toString().isBlank()
                ||binding.dateOfBirth.text.toString().isBlank()
                ||binding.homeTown.text.toString().isBlank()
                ||binding.state.text.toString().isBlank()
                ||binding.country.text.toString().isBlank()
                ||binding.phoneNumber.text.toString().isBlank()
                ||binding.telephoneNumber.text.toString().isBlank()
                ||profilePicUrl.toString().isBlank()
                )

    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
                requireContext(),
                this,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    private fun openFilePicker(request: Int) {
        val intent: Intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, request)
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = context?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(cr?.getType(uri))
    }

    private fun resetFields(){
        binding.firstName.setText("")
        binding.lastName.setText("")
        binding.gender.setText("")
        binding.dateOfBirth.setText("")
        binding.homeTown.setText("")
        binding.state.setText("")
        binding.country.setText("")
        binding.phoneNumber.setText("")
        binding.telephoneNumber.setText("")
        binding.profileImage.setImageResource(R.drawable.ic_baseline_image_24)
        profilePicUrl = "".toUri()

    }

    private fun setUpAutoCompleteTextView(){
        val genderAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.gender,
                R.layout.autocomplete_text
        )
        binding.gender.threshold = 0
        binding.gender.dropDownVerticalOffset = 20
        binding.gender.setAdapter(genderAdapter)
        binding.gender.setOnItemClickListener { parent, view, position, id ->
            binding.gender.setText(genderAdapter.getItem(position))
        }
    }

    // OVERRIDED FUNCTIONS
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$month/$dayOfMonth/$year"
        binding.dateOfBirth.setText(date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UPLOAD_PIC && resultCode == Activity.RESULT_OK){
            if(data != null){
                profilePicUrl = data.data!!
                Glide.with(requireContext())
                        .load(profilePicUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.profileImage)

            }
        }
    }
}