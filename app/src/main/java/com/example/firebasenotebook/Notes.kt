package com.example.firebasenotebook

import com.google.firebase.firestore.Exclude

class Notes (val title:String,val description:String) {
    constructor():this("","")
    @Exclude
    val id = ""
}