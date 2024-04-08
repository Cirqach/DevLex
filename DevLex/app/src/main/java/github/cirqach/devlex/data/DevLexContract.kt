package github.cirqach.devlex.data

import android.provider.BaseColumns

class DevLexDatabaseContract {

    object LexiconEntry : BaseColumns {
        const val TABLE_NAME = "lexicon_table"
        const val _ID = BaseColumns._ID
        const val WORD_DEFENITION = "WORD_DEFENITION"
        const val ENGLISH_NAME = "ENGLISH_NAME"
        const val RUSSIAN_NAME = "RUSSIAN_NAME"
    }

    object TestTrueFalseEntry : BaseColumns {
        const val TABLE_NAME = "test_true_false_table"
        const val ID = BaseColumns._ID
        const val RESULT = "RESULT"
        const val RESULT_PROCENT = "RESULT_PROCENT"
        const val TIME_START = "TIME_START"
        const val TIME_FINISH = "TIME_FINISH"
        const val PROGRESS = "PROGRESS"
    }

    object TestFindWordEntry : BaseColumns {
        const val TABLE_NAME = "test_find_word_table"
        const val ID = BaseColumns._ID
        const val RESULT = "RESULT"
        const val RESULT_PROCENT = "RESULT_PROCENT"
        const val TIME_START = "TIME_START"
        const val TIME_FINISH = "TIME_FINISH"
        const val PROGRESS = "PROGRESS"
    }

    object TestFindTranslationEntry : BaseColumns {
        const val TABLE_NAME = "test_find_translation_table"
        const val ID = BaseColumns._ID
        const val RESULT = "RESULT"
        const val RESULT_PROCENT = "RESULT_PROCENT"
        const val TIME_START = "TIME_START"
        const val TIME_FINISH = "TIME_FINISH"
        const val PROGRESS = "PROGRESS"
    }
}