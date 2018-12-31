package com.enpassio.apis.googlespreadsheet.model

import java.util.*

/**
 * Created by Abhishek Raj on 12/31/2018.
 */
data class Employee(val name: String,
                    val email: String,
                    val dateOfBirth: Date,
                    val salary: Double)