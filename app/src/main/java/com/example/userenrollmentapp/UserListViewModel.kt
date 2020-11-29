package com.example.userenrollmentapp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class UserListViewModel: ViewModel() {
    private val repo = UserListRepo()
    var usersList = MutableLiveData<MutableList<User>>()
//    val usersList:LiveData<MutableList<User>>
//        get() = _usersList

    var currentUserImageUrl = MutableLiveData<String>()
    fun getUsers(){
       usersList = repo.fetchUsers()
    }

    fun deleteUser(id: String){
        repo.deleteUser(id)
        val temp = usersList.value!!.filter {
            (it.id != id)
        }
        temp.sortedByDescending { it.dateOfCreation }
        usersList.value = temp as MutableList<User>
    }

    fun addUser(user: User){
        repo.addUser(user)
        Log.d("TAG : ViewModel", "addUser: $user")
        val temp = usersList.value
        temp!!.add(user)
        temp.sortByDescending { it.dateOfCreation }
        usersList.value = temp
    }

    fun checkDuplicatePhoneNumber(number: String): Boolean{
        return repo.checkDuplicatePhoneNumber(number)
    }

    fun uploadImage(storeReference: StorageReference, profileImageUrl: Uri, fileExtension: String?,fileName: String){
        Log.d("TAG", "uploadImage: " + profileImageUrl)
        val stationStorageReference = storeReference.child("ProfileImages").child(
            "$fileName.$fileExtension"
        )

        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                stationStorageReference.putFile(profileImageUrl).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    stationStorageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUserImageUrl.value = task.result.toString()
                    }
                }
                return@withContext
            }
            return@launch
        }
    }




}