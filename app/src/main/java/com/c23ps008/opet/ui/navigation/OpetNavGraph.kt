package com.c23ps008.opet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.c23ps008.opet.ui.screen.allpet.AllPetDestination
import com.c23ps008.opet.ui.screen.allpet.AllPetScreen
import com.c23ps008.opet.ui.screen.calculated_pet_result.CalculatedPetResultDestination
import com.c23ps008.opet.ui.screen.calculated_pet_result.CalculatedPetResultScreen
import com.c23ps008.opet.ui.screen.find_match_cat.FindMatchCatDestination
import com.c23ps008.opet.ui.screen.find_match_cat.FindMatchCatScreen
import com.c23ps008.opet.ui.screen.find_match_dog.FindMatchDogDestination
import com.c23ps008.opet.ui.screen.find_match_dog.FindMatchDogScreen
import com.c23ps008.opet.ui.screen.get_started.GetStartedDestination
import com.c23ps008.opet.ui.screen.get_started.GetStartedScreen
import com.c23ps008.opet.ui.screen.home.HomeDestination
import com.c23ps008.opet.ui.screen.home.HomeScreen
import com.c23ps008.opet.ui.screen.login.LoginDestination
import com.c23ps008.opet.ui.screen.login.LoginScreen
import com.c23ps008.opet.ui.screen.map_nearby_pet.MapNearbyPetDestination
import com.c23ps008.opet.ui.screen.map_nearby_pet.MapNearbyPetScreen
import com.c23ps008.opet.ui.screen.my_post.MyPostDestination
import com.c23ps008.opet.ui.screen.my_post.MyPostScreen
import com.c23ps008.opet.ui.screen.pet_detail.PetDetailDestination
import com.c23ps008.opet.ui.screen.pet_detail.PetDetailScreen
import com.c23ps008.opet.ui.screen.post_camera.ConfirmImageDestination
import com.c23ps008.opet.ui.screen.post_camera.ConfirmImageScreen
import com.c23ps008.opet.ui.screen.post_camera.PostCameraDestination
import com.c23ps008.opet.ui.screen.post_camera.PostCameraScreen
import com.c23ps008.opet.ui.screen.search_breed.ProcessingImageDestination
import com.c23ps008.opet.ui.screen.post_pet.PostPetDestination
import com.c23ps008.opet.ui.screen.post_pet.PostPetScreen
import com.c23ps008.opet.ui.screen.post_pet.UploadPetSuccessDestination
import com.c23ps008.opet.ui.screen.post_pet.UploadPetSuccessScreen
import com.c23ps008.opet.ui.screen.profile.ProfileDestination
import com.c23ps008.opet.ui.screen.profile.ProfileScreen
import com.c23ps008.opet.ui.screen.register.RegisterDestination
import com.c23ps008.opet.ui.screen.register.RegisterScreen
import com.c23ps008.opet.ui.screen.search_breed.ProcessingImageScreen
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedDestination
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedScreen
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedCameraDestination
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedCameraScreen
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedConfirmImageDestination
import com.c23ps008.opet.ui.screen.search_breed.SearchBreedConfirmImageScreen
import com.c23ps008.opet.ui.screen.splash.SplashDestination
import com.c23ps008.opet.ui.screen.splash.SplashScreen
import com.c23ps008.opet.ui.screen.update_pet.UpdatePetDestination
import com.c23ps008.opet.ui.screen.update_pet.UpdatePetScreen
import java.net.URLEncoder

@Composable
fun OPetNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = SplashDestination.route,
        modifier = modifier
    ) {
        composable(SplashDestination.route) {
            SplashScreen(
                navigateToGetStarted = {
                    navController.popBackStack()
                    navController.navigate(GetStartedDestination.route)
                },
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(HomeDestination.route)
                })
        }
        composable(GetStartedDestination.route) {
            GetStartedScreen(navigateToLogin = { navController.navigate(LoginDestination.route) })
        }
        composable(LoginDestination.route) {
            LoginScreen(
                navigateToRegister = { navController.navigate(RegisterDestination.route) },
                navigateToHome = {
                    navController.popBackStack(GetStartedDestination.route, inclusive = true)
                    navController.navigate(HomeDestination.route)
                })
        }
        composable(RegisterDestination.route) {
            RegisterScreen(
                navigateToLogin = { navController.navigate(LoginDestination.route) },
                onNavigateUp = { navController.navigateUp() })
        }
        composable(HomeDestination.route) {
            HomeScreen(
                navigateToProfile = { navController.navigate(ProfileDestination.route) },
                navigateToMapNearby = { navController.navigate(MapNearbyPetDestination.route) },
                navigateToDetail = { navController.navigate("${PetDetailDestination.route}/$it") },
                navigateToViewAllPet = { navController.navigate(AllPetDestination.route) },
                navigateToMyPost = { navController.navigate(MyPostDestination.route) },
                navigateToPostPet = { navController.navigate(PostCameraDestination.route) },
                navigateToFindMatchDog = { navController.navigate(FindMatchDogDestination.route) },
                navigateToFindMatchCat = { navController.navigate(FindMatchCatDestination.route) },
                navigateToSearchBreed = { navController.navigate(SearchBreedDestination.route) })
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToGetStarted = {
                    navController.popBackStack(HomeDestination.route, inclusive = true)
                    navController.popBackStack(MyPostDestination.route, inclusive = true)
                    navController.navigate(GetStartedDestination.route) {
                        popUpTo(HomeDestination.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(
            route = PostPetDestination.routeWithArgs,
            arguments = listOf(navArgument(PostPetDestination.imageUriArg) {
                type = NavType.StringType
            })
        ) {
            PostPetScreen(navigateToUploadPetSuccess = {
                navController.navigate(UploadPetSuccessDestination.route) {
                    popUpTo(PostCameraDestination.route) {
                        inclusive = true
                    }
                }
            },
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
        composable(UploadPetSuccessDestination.route) {
            UploadPetSuccessScreen(navigateToMyPost = {
                navController.popBackStack()
                navController.navigate(MyPostDestination.route)
            })
        }
        composable(
            route = PetDetailDestination.routeWithArgs,
            arguments = listOf(navArgument(PetDetailDestination.petIdArg) {
                type = NavType.StringType
            })
        ) {
            PetDetailScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(AllPetDestination.route) {
            AllPetScreen(onNavigateUp = { navController.navigateUp() }, navigateToDetail = {
                navController.navigate("${PetDetailDestination.route}/$it")
            },
                navigateToSearchBreed = { navController.navigate(SearchBreedDestination.route) }
            )
        }
        composable(MyPostDestination.route) {
            MyPostScreen(
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToDetail = { navController.navigate("${PetDetailDestination.route}/$it") },
                navigateToPostPet = { navController.navigate(PostCameraDestination.route) },
                navigateToEdit = { navController.navigate("${UpdatePetDestination.route}/$it") }
            )
        }
        composable(
            route = UpdatePetDestination.routeWithArgs,
            arguments = listOf(navArgument(UpdatePetDestination.petIdArg) {
                type = NavType.StringType
            })
        ) {
            UpdatePetScreen(onNavigateUp = { navController.navigateUp() })
        }
        composable(PostCameraDestination.route) {
            PostCameraScreen(onCaptureSuccess = {
                navController.navigate(
                    "${ConfirmImageDestination.route}/${
                        URLEncoder.encode(
                            it,
                            "UTF-8"
                        )
                    }"
                )
            }, onNavigateUp = { navController.navigateUp() })
        }
        composable(
            route = ConfirmImageDestination.routeWithArgs,
            arguments = listOf(navArgument(ConfirmImageDestination.imgUriArg) {
                type = NavType.StringType
            })
        ) {
            ConfirmImageScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToProcessingImage = {
                    navController.popBackStack()
                    navController.navigate(
                        "${PostPetDestination.route}/${
                            URLEncoder.encode(
                                it,
                                "UTF-8"
                            )
                        }"
                    )
                })
        }
        composable(MapNearbyPetDestination.route) {
            MapNearbyPetScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToPetDetail = { navController.navigate("${PetDetailDestination.route}/$it") })
        }
        composable(FindMatchDogDestination.route) {
            FindMatchDogScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToCalculatedResult = { breed ->
                    navController.navigate("${CalculatedPetResultDestination.route}/$breed") {
                        popUpTo(SearchBreedCameraDestination.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(FindMatchCatDestination.route) {
            FindMatchCatScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToCalculatedResult = { breed ->
                    navController.navigate("${CalculatedPetResultDestination.route}/$breed") {
                        popUpTo(SearchBreedCameraDestination.route) {
                            inclusive = true
                        }
                    }
                })
        }
        composable(SearchBreedDestination.route) {
            SearchBreedScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToDetail = { navController.navigate("${PetDetailDestination.route}/$it") },
                navigateToSearchBreedCamera = { navController.navigate(SearchBreedCameraDestination.route) }
            )
        }
        composable(SearchBreedCameraDestination.route) {
            SearchBreedCameraScreen(
                onCaptureSuccess = {
                    navController.navigate(
                        "${SearchBreedConfirmImageDestination.route}/${
                            URLEncoder.encode(
                                it, "UTF-8"
                            )
                        }"
                    )
                },
                onNavigateUp = { navController.navigateUp() })
        }
        composable(
            route = SearchBreedConfirmImageDestination.routeWithArgs,
            arguments = listOf(navArgument(SearchBreedConfirmImageDestination.imgUriArg) {
                type = NavType.StringType
            })
        ) {
            SearchBreedConfirmImageScreen(
                onNavigateUp = { navController.navigateUp() },
                navigateToProcessingImage = {
                    navController.navigate(
                        "${ProcessingImageDestination.route}/${
                            URLEncoder.encode(it, "UTF-8")
                        }"
                    )
                })
        }
        composable(
            route = ProcessingImageDestination.routeWithArgs,
            arguments = listOf(navArgument(ProcessingImageDestination.imgUriArg) {
                type = NavType.StringType
            })
        ) {
            ProcessingImageScreen(navigateToCalculatedResult = { petBreed ->
                navController.navigate("${CalculatedPetResultDestination.route}/$petBreed") {
                    popUpTo(SearchBreedCameraDestination.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(
            route = CalculatedPetResultDestination.routeWithArgs,
            arguments = listOf(navArgument(CalculatedPetResultDestination.petBreed) {
                type = NavType.StringType
            })
        ) {
            CalculatedPetResultScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                navigateToDetail = { navController.navigate("${PetDetailDestination.route}/$it") }
            )
        }
    }
}