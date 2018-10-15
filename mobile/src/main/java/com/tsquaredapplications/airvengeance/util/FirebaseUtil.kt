package com.tsquaredapplications.airvengeance.util

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtil {
    companion object {
        fun openDataRef(): DatabaseReference{
            return FirebaseDatabase.getInstance().reference.child("DATA")
        }
    }
}