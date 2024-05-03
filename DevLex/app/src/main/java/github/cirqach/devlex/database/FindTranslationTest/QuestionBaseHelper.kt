package github.cirqach.devlex.database.FindTranslationTest

import android.content.Context
import android.util.Log
import github.cirqach.devlex.database.DataList
import github.cirqach.devlex.database.DevLexDBHelper
import github.cirqach.devlex.database.DevLexDatabaseContract
import kotlin.random.Random

class QuestionBaseHelper() {

    data class QuestionsBase(
        val questionBase: MutableSet<Question?>?,
    ) {
    }

    data class Question(
        val questions: MutableSet<DataList?>?,
    ) {
        var answered: Boolean = false

    }

    fun getQuestionsBase(numberOfQuestions: Int, dbh: DevLexDBHelper?): QuestionsBase? {
        val TAG = "creating questions base"
        val result = QuestionsBase(
            mutableSetOf(
                Question(
                    mutableSetOf(
                        DataList("1", "1", "1", "1")
                    )
                )
            )
        )
        Log.d(TAG, "getQuestionsBase: result created = $result")
        Log.d(TAG, "getQuestionsBase: creating normal result")
        if (result.questionBase != null) {
            while (result.questionBase.size < numberOfQuestions) {
                Log.d(
                    TAG,
                    "getQuestionsBase: result size less than $numberOfQuestions, count of iteration is ${result.questionBase.size}"
                )
                if (dbh != null) {
                    val data = getRandomQuestion(dbh)
                    Log.d(TAG, "getQuestionsBase: now data = $data")
                    if (data != null) {
                        result.questionBase.add(getRandomQuestion(dbh))
                    } else {
                        Log.d(TAG, "getQuestionsBase: data is null")
                    }
                } else {
                    Log.d(TAG, "getQuestionsBase: dbh is null")
                }
                if (result.questionBase?.remove(
                        Question(
                            mutableSetOf(
                                DataList("1", "1", "1", "1")
                            )
                        )
                    ) == true
                ) {
                    Log.d(TAG, "getQuestionsBase: removing temporary data successful")
                } else {
                    Log.d(TAG, "getQuestionsBase: CANT REMOVE temporary data")
                }

                return result
            }
        }
        Log.d(TAG, "getQuestionsBase: SOMETHING IS WRONG returning null")
        return null
    }

    fun getRandomQuestion(
        dbh: DevLexDBHelper?
    ): Question? {
        val TAG = "creating random question"
        if (dbh != null) {
            return Question(
                mutableSetOf( // getting DataList by random question three times
                    dbh.getDataByIdFromLexicon(
                        DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                        Random.nextInt(
                            1,
                            DevLexDBHelper.getTableRowCount(
                                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                                dbh
                            )
                        )
                    ),
                    dbh.getDataByIdFromLexicon(
                        DevLexDatabaseContract.LexiconEntry.TABLE_NAME, Random.nextInt(
                            1,
                            DevLexDBHelper.getTableRowCount(
                                DevLexDatabaseContract.LexiconEntry.TABLE_NAME, dbh
                            )
                        )
                    ),
                    dbh.getDataByIdFromLexicon(
                        DevLexDatabaseContract.LexiconEntry.TABLE_NAME, Random.nextInt(
                            1,
                            DevLexDBHelper.getTableRowCount(
                                DevLexDatabaseContract.LexiconEntry.TABLE_NAME, dbh
                            )
                        )
                    )
                )
            )
        } else {
            Log.d(TAG, "getRandomQuestion: dbh is null")
        }
        return null
    }

}

/* if (dbh != null) {
            val randomNumber: Int =
                Random.nextInt(1, DevLexDBHelper.getTableRowCount(TABLE_NAME, dbh))
            Log.d(
                DevLexDBHelper.TAG,
                "getRandomIdFromTable: randoming number from 1 to ${
                    DevLexDBHelper.getTableRowCount(
                        TABLE_NAME,
                        dbh
                    )
                }, number is $randomNumber"
            )
            if (blackList.contains(randomNumber)) {
                Log.d(DevLexDBHelper.TAG, "getRandomIdFromTable: $blackList contains $randomNumber")
                getRandomIdFromTable(TABLE_NAME, blackList, dbh)
            } else {
                Log.d(DevLexDBHelper.TAG, "getRandomIdFromTable: $randomNumber not in black list")
                Log.d(DevLexDBHelper.TAG, "getRandomIdFromTable: adding $randomNumber to blacklist")
                blackList.add(randomNumber)
                Log.d(DevLexDBHelper.TAG, "getRandomIdFromTable: trying to get items from database")
                val result = dbh.getDataByIdFromLexicon(TABLE_NAME, randomNumber)
                Log.d(DevLexDBHelper.TAG, "getRandomIdFromTable: returning $result")
                return result
            }
        } else {
            val TAG = "randomizing id from table"
            Log.d(TAG, "getRandomIdFromTable: dbh is null")
        }
        return null*/