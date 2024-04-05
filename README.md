It's my course project on second semester of second course.
Main idea: create app which will help to learn name of country's on English

Theoretical question: "Строковый и символьный тип данных"

Project theme:

Programming language: "Kotlin"

Platform: "Android/arm"

IDE: "Android studio"

SQL: "SQLite"

Pages:

Home page. Здесь будет название проекта, моё имя и кнопки для перехода на другие страницы
страница на которой будет флаг + название на русском + название на английском и стрелка вниз что бы можно было листать страны, при свапе влево(от правого края) будет открываться более подробная инфа
Information:

functions for work with db https://www.codersarts.com/post/integrating-sqlite-in-android-app-using-kotlin-a-step-by-step-guide
tasks for app https://thegirl.ru/articles/7-prilozhenii-kotorye-pomogut-podtyanut-tvoi-znaniya-po-geografii/
can use for information about country's https://www.cia.gov/the-world-factbook/countries/ also can use this https://en.m.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population
activities: https://www.fandroid.info/urok-5-kotlin-dobavlenie-vtorogo-ekrana-v-android-prilozhenie/
slider: https://www.fandroid.info/18-android-viewpager2/
debugging https://developer.android.com/topic/performance/vitals/crash
viewpager + SQLite https://stackoverflow.com/questions/46686061/how-to-load-sqlite-data-into-a-viewpager
Ideas
добавить подсказки
добавить разные режимы: русские/английские названия; нужно угадать по флагу; нужно угадать по местоположению; выбрать правильный перевод из 6 выборов
для создания псевдо-ленты тик тока можно использовать одну активити, но каждый раз при сварке вниз рандомить страну
для свара вверх можно использовать кэширование redisом
