package github.cirqach.devlex.database.TrueFalseTest

import android.util.Log
import github.cirqach.devlex.database.DataList
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import kotlin.random.Random

class QuestionBaseHelper {
    private val tag = "true false test"

    data class Question(
        val russianOrEnglishName: String,
        val definition: String,
        val answer: Boolean // Change the type to Boolean
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
            if (question != null && question.definition.isNotEmpty()) {
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
        for (attempt in 1..2) {
            try {
                val randomIndex = Random.nextInt(0, tableRowCount)
                val questionData = dbh.getDataByIdFromLexicon(
                    DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                    randomIndex + 1 // Adjust for 0-based indexing
                )
                Log.d(tag, "getRandomQuestion: question data = $questionData")

                if (questionData != null) { // Check if data is retrieved successfully
                    val options = mutableListOf(
                        questionData,
                        getRandomQuestionExceptId(dbh, questionData.id.toInt(), tableRowCount)
                            ?: continue,
                    )

                    options.shuffle()

                    val russianOrEnglishName = Random.nextBoolean()
                    val definition = if (Random.nextBoolean()) {
                        options[0].definition
                    } else {
                        options[1].definition
                    }

                    return Question(
                        if (russianOrEnglishName) {
                            questionData.englishName
                        } else {
                            questionData.russianName
                        },
                        definition,
                        questionData.definition == definition
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