package github.cirqach.devlex.database.FindTranslationTest

import android.util.Log
import github.cirqach.devlex.database.DataList
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import kotlin.random.Random

class QuestionBaseHelper {

    data class Question(
        val englishName: String,
        val russianOption1: String,
        val russianOption2: String,
        val russianOption3: String,
        val answer: Int // The index (1-based) of the correct option
    )


    fun getQuestionsList(dbh: DevLexDBHelper?, questionsCount: Int): MutableList<Question?> {
        if (dbh == null) {
            Log.d("getQuestionsList", "dbh is null")
            return mutableListOf(null) // Return an empty list with a single null element to indicate failure
        }

        val tableRowCount = DevLexDBHelper.getTableRowCount(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            dbh
        )

        if (tableRowCount == 0) {
            Log.d("getQuestionsList", "No questions found in the database")
            return mutableListOf(null) // Return an empty list with a single null element to indicate failure
        }

        val questionsList = mutableListOf<Question?>()
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
                "getQuestionsList",
                "Retrieved only ${questionsList.size} questions out of requested $questionsCount"
            )
        }

        return questionsList
    }


    private fun getRandomQuestion(dbh: DevLexDBHelper?): Question? {
        if (dbh == null) {
            Log.d("getRandomQuestion", "dbh is null")
            return null
        }

        val tableRowCount = DevLexDBHelper.getTableRowCount(
            DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
            dbh
        )

        if (tableRowCount == 0) {
            Log.d("getRandomQuestion", "No questions found in the database")
            return null
        }

        // Try retrieving a random question up to 3 times (adjustable)
        for (attempt in 1..3) {
            try {
                val randomIndex = Random.nextInt(0, tableRowCount)
                val questionData = dbh.getDataByIdFromLexicon(
                    DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                    randomIndex + 1 // Adjust for 0-based indexing
                )


                if (questionData != null) { // Check if data is retrieved successfully
                    val options = mutableListOf(
                        questionData,
                        getRandomQuestionExceptId(dbh, questionData.id.toInt(), tableRowCount)
                            ?: continue,
                        getRandomQuestionExceptId(dbh, questionData.id.toInt(), tableRowCount)
                            ?: continue
                    )

                    options.shuffle()

                    val correctOptionIndex =
                        options.indexOf(questionData) + 1 // +1 for 1-based indexing

                    return Question(
                        questionData.englishName,
                        options[0].russianName,
                        options[1].russianName,
                        options[2].russianName,
                        correctOptionIndex
                    )
                }
            } catch (e: Exception) {
                Log.e("getRandomQuestion", "Error getting random question: ${e.message}")
            }
        }

        Log.d("getRandomQuestion", "Failed to retrieve a random question after 3 attempts")
        return null // Return null after all attempts fail
    }


    private fun getRandomQuestionExceptId(
        dbh: DevLexDBHelper,
        id: Int,
        tableRowCount: Int
    ): DataList? {
        while (true) {
            val randomNumber = Random.nextInt(1, tableRowCount + 1) // Include upper bound
            if (randomNumber != id && dbh.doesIdExist(
                    DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                    randomNumber
                )
            ) {
                return dbh.getDataByIdFromLexicon(
                    DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                    randomNumber
                )
            }
        }
    }
}

