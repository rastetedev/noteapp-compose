package com.rastete.noteappcompose.feature_note.presentation.notes

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rastete.noteappcompose.MainActivity
import com.rastete.noteappcompose.core.util.ContentDescriptionsTags
import com.rastete.noteappcompose.core.util.TestTags
import com.rastete.noteappcompose.di.AppModule
import com.rastete.noteappcompose.feature_note.presentation.util.Screen
import com.rastete.noteappcompose.ui.theme.NoteAppComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            NoteAppComposeTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun clickToggleOrderSection_isVisible() {
        composeTestRule.onNodeWithTag(TestTags.ORDER_SECTION).assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription(ContentDescriptionsTags.SORT_ICON).performClick()
        composeTestRule.onNodeWithTag(TestTags.ORDER_SECTION).assertIsDisplayed()

    }

}