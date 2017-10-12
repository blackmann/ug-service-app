package com.integratorsb2b.ug

import android.databinding.ObservableField

class Util {
    companion object {
        fun isValidPostalAddress(postalAddress: ObservableField<String>): Boolean {
            return postalAddress.get() != null && postalAddress.get().trim().length > 5
        }

        fun isValidStudentNumber(studentNumber: ObservableField<String>): Boolean {
            return studentNumber.get() != null && studentNumber.get().trim().length > 4
        }
    }
}