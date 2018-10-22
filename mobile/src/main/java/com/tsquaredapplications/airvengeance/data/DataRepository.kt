package com.tsquaredapplications.airvengeance.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.tsquaredapplications.airvengeance.util.FirebaseUtil

class DataRepository {
    private var database: DatabaseReference = FirebaseUtil.openDataRef()
    private var dataStream = MutableLiveData<List<Data>>()

    init {

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = mutableListOf<Data>()
                val dataChildren = dataSnapshot.children
                for (child in dataChildren) {
                    val possibleData = child.getValue(Data::class.java)
                    possibleData?.let { nonNullData ->
                        data.add(nonNullData)
                    }
                }
                dataStream.value = data
            }

        })
    }

    fun getDataStream(): MutableLiveData<List<Data>> {
        return dataStream
    }
}