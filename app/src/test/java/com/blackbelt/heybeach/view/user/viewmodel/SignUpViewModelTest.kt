package com.blackbelt.heybeach.view.user.viewmodel

import com.blackbelt.heybeach.R
import com.blackbelt.heybeach.domain.OnDataLoadedListener
import com.blackbelt.heybeach.domain.model.ErrorModel
import com.blackbelt.heybeach.domain.user.IUserManager
import com.blackbelt.heybeach.domain.user.model.SignUpModel
import com.blackbelt.heybeach.view.View
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest {

    @Mock
    private lateinit var mUserManager: IUserManager

    @Mock
    private lateinit var mListener: View<Any>

    private val mSignUpViewModel: SignUpViewModel by lazy {
        SignUpViewModel(mUserManager)
    }

    @Test
    fun test_invalid_email() {
        mSignUpViewModel.email = "aa"
        Assert.assertFalse(mSignUpViewModel.isValidEmail(mSignUpViewModel.email))
    }

    @Test
    fun test_empty_email() {
        mSignUpViewModel.email = ""
        Assert.assertFalse(mSignUpViewModel.isValidEmail(mSignUpViewModel.email))
    }

    @Test
    fun test_valid_email() {
        mSignUpViewModel.email = "test@test.com"
        Assert.assertTrue(mSignUpViewModel.isValidEmail(mSignUpViewModel.email))
    }

    @Test
    fun test_short_password() {
        mSignUpViewModel.password = "1234"
        Assert.assertFalse(mSignUpViewModel.isValidPassword(mSignUpViewModel.password))
    }


    @Test
    fun test_valid_password() {
        mSignUpViewModel.password = "123456"
        Assert.assertTrue(mSignUpViewModel.isValidPassword(mSignUpViewModel.password))
    }

    @Test
    fun test_cant_signup_email() {
        mSignUpViewModel.email = "aa"
        mSignUpViewModel.password = "123456"
        Assert.assertFalse(mSignUpViewModel.canSignUp())
    }

    @Test
    fun test_cant_signup_password() {
        mSignUpViewModel.email = "test@test.com"
        mSignUpViewModel.password = "1236"
        Assert.assertFalse(mSignUpViewModel.canSignUp())
    }

    @Test
    fun test_can_signup() {
        mSignUpViewModel.email = "test@test.com"
        mSignUpViewModel.password = "123456"
        Assert.assertTrue(mSignUpViewModel.canSignUp())
    }

    @Test
    fun test_login_labels() {
        mSignUpViewModel.isLogin = true
        Assert.assertTrue(mSignUpViewModel.getButtonLabel() == R.string.login)
        Assert.assertTrue(mSignUpViewModel.getTitle() == R.string.login)
    }

    @Test
    fun test_signup_labels() {
        mSignUpViewModel.isLogin = false
        Assert.assertTrue(mSignUpViewModel.getButtonLabel() == R.string.signup_for_free)
        Assert.assertTrue(mSignUpViewModel.getTitle() == R.string.sign_up_title)
    }

    @Test
    fun test_on_data_loaded() {

        val signUpModel = SignUpModel()

        Mockito.doAnswer {
            (it.arguments[2] as OnDataLoadedListener<SignUpModel>).onDataLoaded(signUpModel)
        }.`when`(mUserManager).signUp(Mockito.anyString(), Mockito.anyString(), Mockito.any())

        mSignUpViewModel.mListener = mListener
        mSignUpViewModel.email = "test@test.com"
        mSignUpViewModel.password = "123456"
        mSignUpViewModel.doSignUp()

        Mockito.verify(mListener).onDataLoaded(signUpModel)

    }

    @Test
    fun test_on_error() {

        val throwable = Throwable()

        val errorModel = ErrorModel()

        Mockito.doAnswer {
            (it.arguments[2] as OnDataLoadedListener<SignUpModel>).onError(errorModel, throwable)
        }.`when`(mUserManager).signUp(Mockito.anyString(), Mockito.anyString(), Mockito.any())

        mSignUpViewModel.mListener = mListener
        mSignUpViewModel.email = "test@test.com"
        mSignUpViewModel.password = "123456"
        mSignUpViewModel.doSignUp()

        Mockito.verify(mListener).onError(errorModel, throwable)

    }
}