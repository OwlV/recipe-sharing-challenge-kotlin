package net.azeti.challenge.recipe.user

import net.azeti.challenge.recipe.user.model.Login
import net.azeti.challenge.recipe.user.model.Registration
import net.azeti.challenge.recipe.user.model.RegistrationResult
import net.azeti.challenge.recipe.user.model.Token
import java.util.*

interface UserManagement {

    fun register(registration: Registration): RegistrationResult

    fun login(login: Login): Token
}