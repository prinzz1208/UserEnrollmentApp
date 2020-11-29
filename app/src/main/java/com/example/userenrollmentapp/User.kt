package com.example.userenrollmentapp

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
    var id: String?,
    var firstName: String?,
    var lastName: String?,
    var dob: String?,
    var gender: String?,
    var country: String?,
    var state: String?,
    var homeTown: String?,
    var phoneNumber: String?,
    var telePhoneNumber: String?,
    var imageUrl: String?,
    var dateOfCreation: Timestamp?
): Parcelable