package com.tsquaredapplications.airvengeance.objects

import com.google.firebase.database.FirebaseDatabase

class Repository {
    private val dbRef = FirebaseDatabase.getInstance().reference.child("DATA")


    /**
     * Pushes data to the Firebase database
     */
    fun push(data: Data){
        val newRef = dbRef.push()
        newRef.setValue(data)
    }
}