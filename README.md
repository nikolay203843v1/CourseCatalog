# CourseCatalog

Тестовое Android-приложение с каталогом курсов, локальным избранным и экраном
авторизации.

## Возможности

- проверка email и пароля на экране входа;
- блокировка кириллицы в поле email;
- переходы во VK и Одноклассники через системный браузер;
- загрузка курсов из JSON на Google Drive через Retrofit;
- реактивное отображение данных из Room;
- сохранение избранного между запусками приложения;
- сортировка по дате публикации;
- отдельный экран избранных курсов;
- состояния загрузки, пустого списка и ошибки.


## Технологии

- Kotlin, Coroutines и Flow;
- XML, ViewBinding, RecyclerView и AdapterDelegates;
- MVVM и Clean Architecture;
- Retrofit, OkHttp и Gson;
- Room;
- Koin;
- Navigation Component;
- JUnit.

## Модули

```text
app                 точка входа, Application и корневая навигация
core:ui             общая тема, цвета, размеры и иконки
domain:courses      модели, контракты репозитория и use cases
data:courses        Retrofit, Room, маппинг и реализация репозитория
feature:auth        экран и логика авторизации
feature:main        каталог, избранное, аккаунт и нижняя навигация
```

Зависимости направлены внутрь: UI-модули знают о `domain`, `data` реализует
контракт `domain`, а `domain` не зависит от Android Framework.

## Запуск

Требуются JDK 21 и Android SDK 35.

```bash
./gradlew :app:assembleDebug
```

APK будет создан в:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Для входа подойдёт, например:

```text
Email: user@example.com
Пароль: password
```

## Проверки

```bash
./gradlew test
```

Тестами покрыты валидация входа, преобразование сетевой модели и сортировка
курсов. Схема Room экспортируется в `data/courses/schemas`.

Подробное описание решений находится в
[`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
