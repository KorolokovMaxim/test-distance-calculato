ТЕСТОВАЯ ЗАДАЧА НА ВАКАНСИЮ JAVA JUNIOR DEVELOPER

Сделал Корольков Максим

<======ВАЖНО=======>

ИЗМЕНИТЬ КОНФИГУРАЦИИ в src/main/resources/application.properties

MySQL login/password - root/root
MySQL database name - distance-calculator

file.upload-dir - в файле application.properties - нужно указать абсолютный путь к пакету
                  куда сохраняется XML файл (в самом файле есть пример как это выглядит , указать путь до пакета src)
<====ВАЖНО========>


TOOLS
    - Maven
    - MySQL
    - Liquibase
    - JAXB
    - Java 11
    - Spring Boot

Описание тестовой задачи

    - Разработать REST приложение для подсчета дистанций между городами тремя способами
    - Crowflight
    - Distance Matrix
    - All

    Приложение должно работать с базой данных и должно иметь две таблицы
    - City
      - Name
      - Latitude
      - Longitude

    - Distance
     - from_city
     - to_city
     - distance

    За работу с таблицам отвечает система управления версиями Liquibase

    Приложение должно получать список городов с помощью загрузки XML файла
    сканировать содержимое файла и сохранять в базу данных. Так же происходит копирования файла в папку проекта
    (Загрузка производиться POST методом , через Postman или т.п.)

    Приложение должно уметь :
        - Вывод всех ID и NAME полей из таблицы City
        - Рассчет расстояния тремя методоми и вывод результата
            - crowflight(Список городов)
            - distance matrix(Список городов)
            - all(Список городов)
        - Загрузку данных в базу данных из XML файла


    Загрузить данные из файла в базу данных
        - В Postman или любой другой программе выбрать:
            URL: localhost:8080/upload
            Метод: POST
            Key: file (тип File)



    Примеры работы программы:
        - Показать все города из БД
            - http://localhost:8080/show-all-city
        - Показать одну или несколько дистанций методом crowflight
            -     http://localhost:8080/get-distance?method=cf&from=Samara,Irkutsk&to=Tver,Moscow
            дистанции разделяються запятой
        - Показать одну или несколько дистанций методом Distance Matrix
             -    http://localhost:8080/get-distance?method=md&from=Samara,Irkutsk&to=Tver,Moscow
        - Показать одну или несколько дистанций обоими способами
             -    http://localhost:8080/get-distance?method=all&from=Samara,Irkutsk&to=Tver,Moscow


Вроде все) это моя первая документация.



