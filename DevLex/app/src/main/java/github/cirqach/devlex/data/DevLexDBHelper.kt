package github.cirqach.devlex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class DevLexDBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private fun checkDataBase(context: Context): Boolean {
        val dbPath = context.applicationInfo.dataDir + DATABASE_NAME
        val dbFile = File(dbPath)
        return dbFile.exists()
    }

    override fun onCreate(db: SQLiteDatabase) {

        Log.d(LOG_TAG, "onCreate: path to database " + db.path)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        Log.d(LOG_TAG, "onUpgrade: CALLED ON UPGRADE")

    }

    companion object {
        val LOG_TAG: String = DevLexDBHelper::class.java.getSimpleName()

        private const val DATABASE_NAME = "DevLex.db"

        private const val DATABASE_VERSION = 1
    }

    // add data to Lexicon
    fun addDataToLexicon(english_name: String, russian_name: String, defenition: String): Boolean {
        val p0 = this.writableDatabase
        val content_values = ContentValues()
        content_values.put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, english_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russian_name)
        content_values.put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, defenition)
        val result = p0.insert(DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null, content_values)
        return result != -1.toLong()
    }

    // read all data from lexicon
    // TODO: read from any table
    fun readAllDataFromLexicon(): Cursor? {
        val p0 = this.writableDatabase
        val cursor =
            p0.rawQuery("select * from " + DevLexDatabaseContract.LexiconEntry.TABLE_NAME, null)
        return cursor
    }


    // TODO: delete by ID from any table
    fun deleteData(TABLE_NAME: String, ENGLISH_NAME: String) {
        val db = this.writableDatabase

        // delete the data from the table
        db.delete(TABLE_NAME, "ENGLISH_NAME = ?", arrayOf(ENGLISH_NAME))

        // close the database connection
        db.close()
    }

    fun saveDataToLexicon(
        englishName: String,
        russianName: String,
        definition: String,
        id: String
    ) {
        Log.d(LOG_TAG, "Start saveData")

        // 1. Check for valid database access:
        val db = writableDatabase
        if (db == null || !db.isOpen) {
            Log.d(LOG_TAG, "Database not open for update")
            return  // Exit the function if database is unavailable
        }

        try {
            // 2. Prepare content values:
            val contentValues = ContentValues().apply {
                put(DevLexDatabaseContract.LexiconEntry.ENGLISH_NAME, englishName)
                put(DevLexDatabaseContract.LexiconEntry.RUSSIAN_NAME, russianName)
                put(DevLexDatabaseContract.LexiconEntry.WORD_DEFENITION, definition)
            }
            Log.d("save data fun", "saveData: content values = $contentValues")

            // 3. Update the database:
            val whereClause = "${DevLexDatabaseContract.LexiconEntry.ID} = ?"
            val whereArgs = arrayOf(id)
            val rowsUpdated = db.update(
                DevLexDatabaseContract.LexiconEntry.TABLE_NAME,
                contentValues,
                whereClause,
                whereArgs
            )

            Log.d(LOG_TAG, "Rows updated: $rowsUpdated")
            if (rowsUpdated == 0) {
                Log.d(LOG_TAG, "No rows updated, data might be unchanged or entry not found")
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error saving data: $e")
            // Handle potential exceptions during database operations (optional)
        } finally {
            // 4. Always close the database connection:
            db.close()
        }
    }

    /*

        fun getDataByID(TABLE_NAME: String, ID:String): DataList{
            val db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE ${DevLexDatabaseContract.LexiconEntry.ID} = ${ID.toInt()}"
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()

            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val english_name = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val russian_name = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))
            val defenition = cursor.getInt(cursor.getColumnIndexOrThrow(DevLexDatabaseContract.LexiconEntry.ID))

        }
    */


    // WTF
    fun fillDataBase(db: SQLiteDatabase) {


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