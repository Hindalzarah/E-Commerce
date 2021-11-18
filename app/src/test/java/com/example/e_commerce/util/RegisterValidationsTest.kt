package com.example.e_commerce.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RegisterValidationsTest{

    private lateinit var validator: RegisterValidations

    //this functions works before the text
    @Before
    fun setup(){
        validator = RegisterValidations()
    }


    @Test
    fun emailIsValidWithInValidEmailThenReturnFalseValue(){

        val validation = validator.emailIsValid("test-dd-com")
        // to check if the email matches
        assertEquals(false,validation)
    }

    @Test
    fun emailIsValidWithValidEmailThenReturnTrue(){
        val validation = validator.emailIsValid("test@dd.com")
        assertEquals(true,validation)
    }

    @Test
    fun passwordIsValidwithInvalidPasswordThenReturnFalseValue(){
        val validation = validator.passwordIsValid("19")
        assertEquals(false,validation)
    }

    @Test
    fun passwordIsValidWithValidPasswordThenReturnFalseValue(){

        val validation = validator.passwordIsValid("Tu@21956830")


        assertEquals(true,validation)
    }
}