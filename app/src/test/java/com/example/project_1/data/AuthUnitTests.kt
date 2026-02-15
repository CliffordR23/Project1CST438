package com.example.project_1.data

import android.content.Context
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.*
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class AuthUnitTests {

    private lateinit var ctx: Context

    @Before
    fun setUp() {
        ctx = ApplicationProvider.getApplicationContext()
        // wipe prefs so no one starts logged in
        ctx.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit().clear().apply()

        // wipe db file and reset singleton so each db is empty
        ctx.deleteDatabase("database")
        AppDatabase.resetForTests()
    }

    @After
    fun cleanUp() {
        // clean again to avoid bleed between tests
        ctx.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit().clear().apply()
        ctx.deleteDatabase("database")
        AppDatabase.resetForTests()
    }

    @Test
    fun signUpCreatesUser() = runBlocking {
        val userId = AuthManager.signUp(ctx, "test@example.com", "password123")
        assertNotNull(userId)

        val stored = AuthManager.getCurrentUserId(ctx)
        assertEquals(userId, stored)
    }

    @Test
    fun signUpDup() = runBlocking {
        val first = AuthManager.signUp(ctx, "test@example.com", "password123")
        assertNotNull(first)

        val second = AuthManager.signUp(ctx, "test@example.com", "password123")
        assertNull(second)
    }

    @Test
    fun loginValidCredentials() = runBlocking {
        val created = AuthManager.signUp(ctx, "test@example.com", "password123")
        assertNotNull(created)

        // clear stored login first
        AuthManager.logout(ctx)
        assertEquals(-1, AuthManager.getCurrentUserId(ctx))

        // checks login returns right userid and if it stores that id
        val loggedIn = AuthManager.login(ctx, "test@example.com", "password123")
        assertEquals(created, loggedIn)
        assertEquals(created, AuthManager.getCurrentUserId(ctx))
    }

    @Test
    fun loginWrongPassword() = runBlocking {
        AuthManager.signUp(ctx, "test@example.com", "password123")
        AuthManager.logout(ctx)
        // login should fail and still be logged out
        val loggedIn = AuthManager.login(ctx, "test@example.com", "WRONG")
        assertNull(loggedIn)
        assertEquals(-1, AuthManager.getCurrentUserId(ctx))
    }

    @Test
    fun loginOrCreateFromGoogle() = runBlocking {
        // checks: creates a userid if google login doesn't already have one
        // calling again returns same userid and pref is updated
        val id = AuthManager.loginOrCreateFromGoogle(ctx, "googleuser@gmail.com")
        assertTrue(id > 0)
        assertEquals(id, AuthManager.getCurrentUserId(ctx))

        // calling again should return SAME user id (no duplicate)
        val id2 = AuthManager.loginOrCreateFromGoogle(ctx, "googleuser@gmail.com")
        assertEquals(id, id2)
    }

    @Test
    fun logoutClearsCurrentUserId() = runBlocking {
        // confirms if log out removes stored login
        val id = AuthManager.signUp(ctx, "test@example.com", "password123")
        assertNotNull(id)
        assertTrue(AuthManager.getCurrentUserId(ctx) != -1)

        AuthManager.logout(ctx)
        assertEquals(-1, AuthManager.getCurrentUserId(ctx))
    }
}