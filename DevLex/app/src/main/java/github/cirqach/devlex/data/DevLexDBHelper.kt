package github.cirqach.devlex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class DevLexDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_WORD_TABLE =
            "CREATE TABLE " + DevLexDatabaseContract.LexiconEntry.TABLE_NAME + " (" +
                    DevLexDatabaseContract.LexiconEntry._ID + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                    DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME + " TEXT NOT NULL , " +
                    DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME + " TEXT NOT NULL , " +
                    DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION + " TEXT);"
        db.execSQL(SQL_CREATE_WORD_TABLE)

        fillDataBase(db)
        Log.d(LOG_TAG, "onCreate: path to database " + db.path)

    }


    fun copyDatabase(context: Context) {
        val dbFolder = context.getDatabasePath("databases")
        if (!dbFolder.exists()) dbFolder.mkdirs()

        val dbFilePath = dbFolder.path + File.separator + "DevLex.db"
        Log.d(LOG_TAG, "Database path created")
        val inputStream: InputStream = context.assets.open("DevLex.db")
        Log.d(LOG_TAG, "Opening database")
        val outputStream = FileOutputStream(dbFilePath)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        Log.d(LOG_TAG, "Database copied successfully")
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.d(LOG_TAG, "onUpgrade: CALLED ON UPGRADE")

    }

    companion object {
        val LOG_TAG: String = DevLexDBHelper::class.java.getSimpleName()

        private const val DATABASE_NAME = "DevLex.db"

        private const val DATABASE_VERSION = 1
    }

    fun addDataToLexicon(english_name: String, russian_name: String, defenition: String): Boolean {
        val p0 = this.writableDatabase
        val content_values = ContentValues()
        content_values.put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, english_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russian_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, defenition)
        val result = p0.insert(DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null, content_values)
        return result != -1.toLong()
    }

    fun readAllDataFromLexicon(): Cursor? {
        val p0 = this.writableDatabase
        val cursor = p0.rawQuery("select * from " + DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null)
        return cursor
    }

    fun updateData() {

    }

    fun deleteData() {

    }

    private fun fillDataBase(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('flow', 'течь/литься', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('mention', 'упоминать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ellipses', 'многоточие', '...');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('tilde', 'тильда', '~');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hyphen/dash', 'дефис', '-');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('at', 'собака', '@');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('underscore', 'нижнее подчеркивание', '_');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('dot', 'точка', '.');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('forward slash', 'косая черта вперёд', '/');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('backward slash', 'косая черта назад', '\\');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('vertical slash', 'вертикальная черта', '|');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('square brackets', 'квадратные скобки', '[]');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('exclamation sign', 'восклицательный знак', '!');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('comma', 'запятая', ',');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('brackets', 'круглые скобки', '()');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('percent', 'процент', '%');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ampersand', 'амперсанд', '&');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('asterisk', 'звёздочка', '*');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('caret', 'циркумфлекс/карет', '^');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('colon', 'двоеточие', ':');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('semicolon', 'точка с запятой', ';');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('double quotes', 'двойные кавычки', '\"');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('single quotes', 'одинарные кавычки', \"'\");\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hash', 'знак решётки', '#');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('dollar sign', 'знак доллара', '$');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('equals', 'равно', '=');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('greater than', 'больше чем', '>');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('less than', 'меньше чем', '<');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('website developer', 'разработчик сайтов', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('create', 'создавать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('analyze', 'анализировать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('database', 'база данных', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('provide', 'обеспечивать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('set up/install', 'устанавливать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data', 'данные', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data processing', 'обработка данных', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('firewall', 'брандмауэр/фаервол', 'это фильтр между компьютером и сетью, который проверяет безопасность входящих и исходящих данных');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('network', 'сеть', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('network administrator', 'сетевой админ', 'поддерживает бесперебойную работу компьютерной техники, локальной сети и программного обеспечения');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('technical', 'технический', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('support', 'поддержка', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('technical support', 'техническая поддержка', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('develop', 'разрабатывать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('workload', 'рабочая нагрузка', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('increase', 'увеличивать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('decrease', 'уменьшать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('divide', 'делить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('information', 'информация', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('security', 'безопасность', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('information security', 'информационная безопасность', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hardware', 'аппаратная часть компьютера', 'физические комплектующие компьютера');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('software', 'программное обеспечение', 'программы устанавливаемые на компьютер');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('encoding', 'кодирование', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('decoding', 'декодирование', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data transfer', 'передача данных', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('maintain', 'хранить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('testing', 'тестирование', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('organize', 'организовывать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('organize data', 'организовывать данные', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('update', 'обновлять', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('security software', 'ПО по обеспечению безопасности', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('reduce', 'снизить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('password', 'пароль', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('wireless', 'беспроводной', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('acronym', 'аббревиатура', 'например: BIOS, http, html');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('username', 'имя пользователя', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('troubleshooting', 'устранение проблем', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('connect', 'соединить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('run', 'запустить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('environment', 'окружение/окружающая среда', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('overwhelmed', 'перегружен', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('avoid', 'избегать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('deadline', 'крайний срок', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('review', 'обзор', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('diagram', 'диаграмма', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('table', 'таблица', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('chart', 'схема/диаграмма', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('graph', 'график', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('connection', 'соединение', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('image', 'изображение', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('resize', 'изменять размер', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('crop', 'обрезать', 'например: обрезать изображение');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('reserve', 'резервировать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('request', 'запрос', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('available', 'доступный', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('notes', 'записи/заметки', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('folder', 'папка', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('suggestion', 'предложение', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('item', 'элемент/пункт', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('replace', 'заменять', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('object', 'объект', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('drag', 'перетаскивать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('supplier', 'поставщик', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('supply', 'поставлять', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('current', 'текущий', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('accept', 'принимать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('output', 'вывод', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('input', 'ввод', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('storage', 'хранение', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('CPU', 'ЦПУ/процессор', 'Central Processing Unit');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('peripherals', 'периферийные устройства', 'например: клавиатура');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hard drive', 'жёсткий диск', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('enable', 'включать/давать возможность', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('rear panel', 'задняя панель', 'например: задняя панель с портами у компьютера');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('plug', 'подключить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('front panel', 'передняя панель', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('connector', 'штекер/соединительный элемент', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('collect', 'собирать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('motherboard', 'материнская плата', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('case', 'корпус', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('fan', 'вентилятор', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('power', 'блок питания', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('cable', 'кабель', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('heatsink', 'радиатор отводящий тепло элемент', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('disconnect', 'отсоединить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('plug in', 'подключить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('unplug', 'отключить', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('screw', 'винт', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('RAM', 'оперативная память', 'Random-Access Memory');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ROM', 'постоянная память', 'Read-Only Memory');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('slot', 'слот', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('optical', 'оптический', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('include', 'включать в себя', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('insert', 'вставлять', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ALU', 'арифметико-логическое устройство', 'Arithmetic Logic Unit');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('counter', 'счетчик', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('system clock', 'системные часы', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('clock speed', 'тактовая частота', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('execute', 'выполнять', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('BIOS', 'базовая система ввода/вывода', 'Basic Input/Output System');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('bus', 'шина', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('bus width', 'разрядность шины', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('unscrew', 'откручивать винты', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Load', 'загружать', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('core', 'ядро', '');\n")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('expand', 'расширять', '');\n")
    }

}