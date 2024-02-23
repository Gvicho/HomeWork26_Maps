package com.example.homework26_maps.presentation.extensions

import android.os.Bundle
import androidx.navigation.NavController

fun NavController.safeNavigateWithArguments(actionId:Int, bundle: Bundle) {
    currentDestination?.getAction(actionId)?.run {
        navigate(actionId,bundle)
    }
}