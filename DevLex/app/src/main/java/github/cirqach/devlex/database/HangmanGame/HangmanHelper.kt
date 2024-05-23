package github.cirqach.devlex.database.HangmanGame

import android.util.Log
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import kotlin.random.Random

class HangmanHelper {

    private val TAG = "Hangman Helper"

    fun getQuestionsList(dbh: DevLexDBHelper?, questionsCount: Int): MutableList<String?> {
        if (dbh == null) {
            Log.d(TAG, "dbh is null")
            return mutableListOf(null) // Return an empty list with a single null element to indicate failure
        }

        val tableRowCount = DevLexDBHelper.getTableRowCount(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            dbh
        )

        if (tableRowCount == 0) {
            Log.d(TAG, "No questions found in the database")
            return mutableListOf(null) // Return an empty list with a single null element to indicate failure
        }

        val questionsList = mutableListOf<String?>()
        var retrievedCount = 0

        while (retrievedCount < questionsCount) {
            val question = getRandomQuestion(dbh)
            if (question != null) {
                questionsList.add(question)
                retrievedCount++
            }
        }

        if (questionsList.size < questionsCount) {
            Log.d(
                TAG,
                "Retrieved only ${questionsList.size} questions out of requested $questionsCount"
            )
        }
        Log.d(TAG, "getQuestionsList: returning $questionsList")
        return questionsList
    }


    private fun getRandomQuestion(dbh: DevLexDBHelper?): String? {
        if (dbh == null) {
            Log.d(TAG, "dbh is null")
            return null
        }

        val tableRowCount = DevLexDBHelper.getTableRowCount(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            dbh
        )

        if (tableRowCount == 0) {
            Log.d(TAG, "No questions found in the database")
            return null
        }

        // Try retrieving a random question up to 3 times (adjustable)
        val randomIndex = Random.nextInt(0, tableRowCount)
        val questionData = dbh.getDataByIdFromLexicon(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            randomIndex + 1 // Adjust for 0-based indexing
        )
        if (questionData != null) {
            return questionData.englishName
        }
        return null
    }


}