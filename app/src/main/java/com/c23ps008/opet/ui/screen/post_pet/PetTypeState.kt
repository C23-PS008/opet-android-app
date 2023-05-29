package com.c23ps008.opet.ui.screen.post_pet

sealed class PetTypeState {
    object Cat: PetTypeState()
    object Dog: PetTypeState()
}