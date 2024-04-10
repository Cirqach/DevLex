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
       /*
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
    */

        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('flow', 'течь/литься', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('mention', 'упоминать', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ellipses', 'многоточие', '...')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('tilde', 'тильда', '~')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hyphen/dash', 'дефис', '-')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('at', 'собака', '@')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('underscore', 'нижнее подчеркивание', '_')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('dot', 'точка', '.')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('forward slash', 'косая черта вперёд', '/')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('backward slash', 'косая черта назад', '\\')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('vertical slash', 'вертикальная черта', '|')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('square brackets', 'квадратные скобки', '[]')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('exclamation sign', 'восклицательный знак', '!')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('comma', 'запятая', ',')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('brackets', 'круглые скобки', '()')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('percent', 'процент', '%')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ampersand', 'амперсанд', '&')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('asterisk', 'звёздочка', '*')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('caret', 'циркумфлекс/карет', '^')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('colon', 'двоеточие', ':')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('semicolon', 'точка с запятой', ';')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('double quotes', 'двойные кавычки', '\"')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('single quotes', 'одинарные кавычки', \"''\")")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hash', 'знак решётки', '#')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('dollar sign', 'знак доллара', '$')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('equals', 'равно', '=')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('greater than', 'больше чем', '>')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('less than', 'меньше чем', '<')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('website developer', 'разработчик сайтов', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('create', 'создавать', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('analyze', 'анализировать', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('database', 'база данных', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('provide', 'обеспечивать', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('set up/install', 'устанавливать', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data', 'данные', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data processing', 'обработка данных', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('firewall', 'брандмауэр/фаервол', 'это фильтр между компьютером и сетью, который проверяет безопасность входящих и исходящих данных')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('network', 'сеть', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('network administrator', 'сетевой админ', 'поддерживает бесперебойное функционирование компьютерной сети')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('server', 'сервер', 'это компьютер, который служит для хранения и обработки данных, к которым могут подключаться другие компьютеры для получения доступа к этим данным')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('client', 'клиент', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('service', 'сервис', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('troubleshoot', 'устранять неполадки', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('software', 'программное обеспечение', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('hardware', 'аппаратное обеспечение', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('access', 'доступ', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('access control', 'контроль доступа', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('bug', 'ошибка', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('debug', 'отлаживать/устранять ошибки', '')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('encryption', 'шифрование', 'процесс преобразования информации в нечитаемый вид для предотвращения несанкционированного доступа')")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('administrator', 'администратор', '')")


        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('IP address', 'IP-адрес', 'это числовой идентификатор каждого устройства в сети');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('username', 'имя пользователя', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('password', 'пароль', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('protocol', 'протокол', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('cloud computing', 'облачные вычисления', 'модель предоставления вычислительных ресурсов через сеть, часто через интернет');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('authentication', 'аутентификация', 'процесс проверки подлинности пользователя');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('authorization', 'авторизация', 'процесс предоставления прав доступа пользователю или приложению');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('malware', 'вредоносное программное обеспечение', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('phishing', 'фишинг', 'мошенничество, целью которого является получение конфиденциальной информации от пользователей, такой как имена пользователей, пароли и информация о кредитных картах');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('ransomware', 'вымогательское программное обеспечение', 'тип вредоносного программного обеспечения, которое блокирует доступ к данным или системе пользователя и требует выкуп за их восстановление');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('antivirus', 'антивирус', 'программное обеспечение, предназначенное для обнаружения и уничтожения компьютерных вирусов');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('spyware', 'шпионское программное обеспечение', 'программное обеспечение, которое собирает информацию о пользователях без их согласия');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('adware', 'рекламное программное обеспечение', 'программное обеспечение, которое отображает рекламу на компьютере пользователя без его согласия');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data breach', 'сбой в безопасности данных', 'несанкционированное доступ к защищенным или конфиденциальным данным');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('botnet', 'ботнет', 'сеть компьютеров, зараженных вредоносным программным обеспечением, которые контролируются удаленным хакером');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('identity theft', 'кража личности', 'преступление, которое включает в себя кражу и использование личных данных каким-либо лицом без разрешения владельца');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('brute force attack', 'атака методом перебора', 'взлом пароля путем последовательного перебора всех возможных комбинаций');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('vulnerability', 'уязвимость', 'слабое место в системе безопасности, которое может быть использовано злоумышленником для атаки');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data privacy', 'конфиденциальность данных', 'право личности на защиту ее личных данных от несанкционированного доступа или использования');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('cybersecurity', 'компьютерная безопасность', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('downtime', 'простой в работе', 'период времени, в течение которого система недоступна или не функционирует');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('uptime', 'время безотказной работы', 'период времени, в течение которого система или компоненты системы работают непрерывно');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('bandwidth', 'пропускная способность', 'мера пропускной способности или возможности передачи данных в сети за определенный период времени');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('data center','дата-центр', 'централизованное устройство, используемое для хранения, обработки и распределения больших объемов данных');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('network', 'сеть', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Internet', 'Интернет', 'глобальная сеть компьютеров, обеспечивающая связь между миллионами компьютеров и других устройств по всему миру');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('router', 'маршрутизатор', 'устройство, используемое для пересылки данных пакетами между сетями');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('server', 'сервер', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('host', 'хост', 'любое устройство, подключенное к сети и имеющее IP-адрес');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('malware', 'вредоносное программное обеспечение', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('spam', 'спам', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('virus', 'вирус', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('encryption', 'шифрование', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('decryption', 'дешифрование', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('authentication', 'аутентификация', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('access control', 'контроль доступа', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('switch', 'коммутатор', '');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('DHCP', 'DHCP', 'Dynamic Host Configuration Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('DNS', 'DNS', 'Domain Name System');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('VPN', 'VPN', 'Virtual Private Network');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('SSH', 'SSH', 'Secure Shell');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('SSL', 'SSL', 'Secure Sockets Layer');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('HTTPS', 'HTTPS', 'Hypertext Transfer Protocol Secure');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('FTP', 'FTP', 'File Transfer Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('HTTP', 'HTTP', 'Hypertext Transfer Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('TCP', 'TCP', 'Transmission Control Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('UDP', 'UDP', 'User Datagram Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('HTML', 'HTML', 'Hypertext Markup Language');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('CSS', 'CSS', 'Cascading Style Sheets');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('SQL', 'SQL', 'Structured Query Language');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('API', 'API', 'Application Programming Interface');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('REST', 'REST', 'Representational State Transfer');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('SOAP', 'SOAP', 'Simple Object Access Protocol');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Microservices', 'Микросервисы', 'архитектурный стиль, в котором комплексное приложение разбивается на небольшие, автономные модули');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('SOA', 'SOA', 'Service-Oriented Architecture');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Serverless Architecture', 'Безсерверная архитектура', 'архитектурный стиль, в котором облачные провайдеры управляют вычислительными ресурсами и масштабируют приложение автоматически');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Code Review', 'Code Review', 'процесс проверки и анализа программного кода');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Refactoring', 'Refactoring', 'процесс изменения внутренней структуры программного кода с целью улучшения его читаемости и сопровождаемости');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Version Control', 'Контроль версий', 'система управления версиями программного обеспечения');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('Git', 'Git', 'распределенная система контроля версий');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('LAN', 'локальная сеть', 'локальная компьютерная сеть');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('WAN', 'глобальная сеть', 'широкополосная сеть, охватывающая большие территории');")
        db.execSQL("INSERT INTO lexicon_table (ENGLISH_NAME, RUSSIAN_NAME, WORD_DEFENITION) VALUES ('subnet mask', 'маска подсети', 'набор бит, определяющий, какая часть IP-адреса относится к сети, а какая к конкретному устройству в сети');")


    }

}