package com.example.userenrollmentapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.withContext

class UserListRepo {
    private val fireStore = FirebaseFirestore.getInstance()
    fun fetchUsers(): MutableLiveData<MutableList<User>> {
        val data = MutableLiveData<MutableList<User>>()
        val usersList = mutableListOf<User>()
        fireStore.collection("Users").orderBy("dateOfCreation", Query.Direction.DESCENDING).get().addOnSuccessListener {
//            log("${it.documents}")
            for (i in 0 until it.documents.size) {

                usersList.add(convertToDataClass(it.documents[i].data!!, User::class.java))
                Log.d("TAG", "fetchUsers: $usersList")
            }
            data.value = usersList
        }
        return data
    }

    fun deleteUser(userId: String){
        fireStore.collection("Users").document(userId).delete()
    }

    fun addUser(user: User){

        val id = fireStore.collection("Users").document().id
        user.id = id
        user.dateOfCreation = Timestamp.now()
        fireStore.collection("Users").document(id).set(user)
        Log.d("TAG", "addUser: User Uploaded")
    }

    fun checkDuplicatePhoneNumber(number: String): Boolean {
        var result = true
        fireStore.collection("Users").whereEqualTo("phoneNumber",number).get().addOnSuccessListener {
            result = !it.isEmpty
        }
        return result
    }
}