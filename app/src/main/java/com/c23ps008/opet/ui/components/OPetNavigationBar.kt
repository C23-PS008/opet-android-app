package com.c23ps008.opet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.c23ps008.opet.R
import com.c23ps008.opet.ui.screen.home.HomeDestination
import com.c23ps008.opet.ui.screen.my_post.MyPostDestination

@Composable
fun OPetNavigationBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    navigateToHome: () -> Unit = {},
    navigateToMyPost: () -> Unit = {},
    navigateToPostPet: () -> Unit = {},
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currentRoute == HomeDestination.route,
            onClick = navigateToHome,
            icon = { Icon(Icons.Outlined.Home, "home_page") },
            label = {
                Text(
                    text = stringResource(R.string.home)
                )
            })
        NavigationBarItem(
            selected = false,
            onClick = navigateToPostPet,
            icon = { Icon(Icons.Outlined.AddCircle, "home_page") },
            label = {
                Text(
                    text = stringResource(R.string.post_pet)
                )
            })
        NavigationBarItem(
            selected = currentRoute == MyPostDestination.route,
            onClick = navigateToMyPost,
            icon = { Icon(Icons.Outlined.ListAlt, "home_page") },
            label = {
                Text(
                    text = stringResource(R.string.my_posts)
                )
            })
    }
}

@Preview(showBackground = true)
@Composable
fun OPetNavigationBarPreview() {
    OPetNavigationBar(currentRoute = "")
}