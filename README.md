# SpaceTravel — Hibernate, H2 та Flyway (модуль 12)

У цьому домашньому завданні я зробив невеликий проєкт для компанії SpaceTravel. У базі є клієнти, планети та квитки. Структуру БД створює Flyway, а для роботи з клієнтами й планетами використовую Hibernate.

## Що зроблено

- `V1__create_db.sql` — таблиці `client`, `planet`, `ticket`, первинні та зовнішні ключі, перевірки довжини імен і формату ID планети.
- `V2__populate_db.sql` — 10 клієнтів, 5 планет і 10 квитків.
- `Client`, `Planet` і `Ticket` — Hibernate-сутності. Для `Client` та `Planet` реалізовано повний CRUD.
- `ClientCrudService` / `PlanetCrudService` — інтерфейси, а `ClientCrudServiceImpl` / `PlanetCrudServiceImpl` перевіряють вхідні дані.
- У пакеті `dao` — інтерфейси та їх Hibernate-реалізації. Тут знаходяться сесії, транзакції та звернення до БД.
- `SpaceTravelSelfTest` перевіряє міграції, дані, зв'язки, CRUD і некоректні значення.

Зробив окремі DAO та сервіси, щоб не змішувати роботу з БД та валідацію. Цей підхід ми вже використовували після коментарів ментора у восьмому модулі.

## Використані технології

- Java 17+ (працюю на JDK 26.0.1);
- Gradle 9.4.1;
- H2 `2.5.252`;
- Hibernate ORM `6.6.59.Final` (анотації `jakarta.persistence`);
- Flyway `13.12.1`.

## Структура БД

`client`: `id` — `BIGINT IDENTITY`, `name` — від 3 до 200 символів.

`planet`: `id` — лише латинські великі літери та цифри, `name` — від 1 до 500 символів. Для зберігання ID використано `VARCHAR(255)`.

`ticket`: `id` — `BIGINT IDENTITY`, `created_at` — `TIMESTAMP` (значення записуються як UTC), `client_id`, `from_planet_id`, `to_planet_id` — зовнішні ключі на таблиці `client` та `planet`.

У квитків зовнішні ключі без `ON DELETE CASCADE`: не можна випадково видалити клієнта чи планету, на яку вже є квитки.

## Як запустити

Розпакувати проєкт у `F:\ProjectsTEST\Java-Gradle-Hibernate-SpaceTravel` та відкрити в IntelliJ IDEA.

У початковому ZIP відсутній Gradle Wrapper, тому спершу копіюю його з попереднього проєкту. Після додавання цих файлів у GitHub повторно копіювати нічого не треба.

```powershell
cd "F:\ProjectsTEST\Java-Gradle-Hibernate-SpaceTravel"
Copy-Item "..\Java-Gradle-Thymeleaf-Time-Cookies\gradlew" . -Force
Copy-Item "..\Java-Gradle-Thymeleaf-Time-Cookies\gradlew.bat" . -Force
New-Item -ItemType Directory -Force ".\gradle\wrapper" | Out-Null
Copy-Item "..\Java-Gradle-Thymeleaf-Time-Cookies\gradle\wrapper\*" ".\gradle\wrapper\" -Force
```

Потім запускаю:

```powershell
.\gradlew.bat clean check
.\gradlew.bat migrateDb
.\gradlew.bat run
```

`check` використовує окрему БД H2 у пам'яті і не зачіпає робочі дані. `migrateDb` створює локальну базу в `data/spacetravel.mv.db` та запускає дві міграції. `run` показує кількість записів і демонструє створення, читання, зміну та видалення тимчасових клієнта й планети.

На першому запуску `migrateDb` очікую повідомлення про застосування **двох** міграцій. При наступному запуску Flyway має показати `0` нових міграцій — це правильно, він не запускає їх повторно.

## Перевірки

У `SpaceTravelSelfTest` є:

- перевірка двох міграцій та відсутності повторного виконання;
- перевірка 10 клієнтів, 5 планет, 10 квитків та зв'язків між ними;
- CRUD-операції для клієнтів і планет;
- перевірка невалідних імен та ID;
- перевірка `CHECK` у H2 і заборони квитка з неіснуючим клієнтом.

Всі SQL-міграції лежать в `src/main/resources/db/migration/`. Hibernate **не створює таблиці автоматично**: `hibernate.hbm2ddl.auto=validate` лише звіряє свої сутності з міграціями.

Клас `Ticket` додано для мапінгу зв'язків, але CRUD-сервіси для квитків не писав, оскільки в завданні цього немає. Значення `created_at` у міграції задані як UTC, а не як локальний час ПК.

## GitHub

У репозиторії мають бути вихідні Java-класи, обидві міграції, `hibernate.properties`, `build.gradle`, `settings.gradle`, README та Gradle Wrapper. Папки `.gradle`, `build`, `.idea`, `data` та файли H2 не завантажуються завдяки `.gitignore`.
