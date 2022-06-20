package com.rastete.noteappcompose.feature_note.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rastete.noteappcompose.MainActivity
import com.rastete.noteappcompose.core.util.ContentDescriptionsTags
import com.rastete.noteappcompose.core.util.TestTags
import com.rastete.noteappcompose.di.AppModule
import com.rastete.noteappcompose.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.rastete.noteappcompose.feature_note.presentation.notes.NotesScreen
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
class NotesEndToEndTest {

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
                    composable(
                        route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(name = "noteColor") {
                                nullable = true
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val color = it.arguments?.getString("noteColor")
                        AddEditNoteScreen(navController = navController, noteColor = color)
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {

        with(composeTestRule) {
            //CLICK ADD NOTE BUTTON
            onNodeWithContentDescription(ContentDescriptionsTags.ADD_FLOATING_BUTTON)
                .performClick()

            //ENTER TEXTS
            onNodeWithTag(TestTags.TITLE_NOTE_TEXT_FIELD).performTextInput(title)
            onNodeWithTag(TestTags.CONTENT_NOTE_TEXT_FIELD).performTextInput(content)
            //CLICK SAVE NOTE BUTTON
            onNodeWithContentDescription(ContentDescriptionsTags.SAVE_FLOATING_BUTTON)
                .performClick()

            //ASSERT NOTE EXISTS
            onNodeWithText(title).assertIsDisplayed()
            //GO TO EDIT NOTE
            onNodeWithText(title).performClick()

            //ASSERT NOTE IS CORRECT FULL FILLED
            onNodeWithTag(TestTags.TITLE_NOTE_TEXT_FIELD).assertTextEquals(title)
            onNodeWithTag(TestTags.CONTENT_NOTE_TEXT_FIELD).assertTextEquals(content)
            //EDIT NOTE TITLE
            onNodeWithTag(TestTags.TITLE_NOTE_TEXT_FIELD).performTextInput(titleModified)
            //CLICK SAVE NOTE BUTTON WITH SOME CHANGES
            onNodeWithContentDescription(ContentDescriptionsTags.SAVE_FLOATING_BUTTON)
                .performClick()

            //ASSERT NOTE WAS EDITED
            onNodeWithText(titleModified).assertIsDisplayed()
        }
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        with(composeTestRule) {
            for (i in 1..3) {
                onNodeWithContentDescription(ContentDescriptionsTags.ADD_FLOATING_BUTTON)
                    .performClick()

                //ENTER TEXTS
                onNodeWithTag(TestTags.TITLE_NOTE_TEXT_FIELD).performTextInput("$title $i")
                onNodeWithTag(TestTags.CONTENT_NOTE_TEXT_FIELD).performTextInput("$content $i")
                //CLICK SAVE NOTE BUTTON
                onNodeWithContentDescription(ContentDescriptionsTags.SAVE_FLOATING_BUTTON)
                    .performClick()
            }

            for(i in 1..3){
                onNodeWithText("$title $i").assertIsDisplayed()
            }

            onNodeWithContentDescription(ContentDescriptionsTags.SORT_ICON).performClick()
            onNodeWithContentDescription(ContentDescriptionsTags.TITLE_RADIO_BUTTON).performClick()
            onNodeWithContentDescription(ContentDescriptionsTags.DESCENDING_RADIO_BUTTON).performClick()

            onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
                .assertTextContains("$title 3")

            onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
                .assertTextContains("$title 2")

            onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
                .assertTextContains("$title 1")
        }

    }

    companion object {
        const val title = "test-title"
        const val content = "test-content"
        const val titleModified = "title-modified"
    }
}