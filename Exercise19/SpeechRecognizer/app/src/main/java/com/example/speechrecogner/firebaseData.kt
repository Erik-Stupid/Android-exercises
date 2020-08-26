package com.example.speechrecogner

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class FirebaseData {
    var time: String? = null
    var message: String? = null

    // Default constructor
    constructor(time: String?, message: String?) {
        this.time = time
        this.message = message
    }
    // Default constructor for Firebase
    constructor() {}
}