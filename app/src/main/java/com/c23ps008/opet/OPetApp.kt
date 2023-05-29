package com.c23ps008.opet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.c23ps008.opet.ui.navigation.OPetNavGraph

@Composable
fun OPetApp(navController: NavHostController = rememberNavController()) {
    OPetNavGraph(navController = navController)
}