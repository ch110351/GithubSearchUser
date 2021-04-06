package com.example.githubsearchuser.widget


import android.view.View

interface ItemClick<T> {
    fun onClicked(view: View, t: T)
}