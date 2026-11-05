# Cookie Lab — React + Spring Boot

Учебный проект для изучения cookies: браузерное хранение, отправка серверу, `HttpOnly`, CORS и `credentials`.

- `frontend/` — React + TypeScript + Vite 7.
- `backend/` — Kotlin + Spring Boot, REST API, Gradle Kotlin DSL (`build.gradle.kts`).
- API устанавливает `demoVisible` и `demoHttpOnly`, а endpoint inspect показывает, какие cookies браузер отправил серверу.
- Подробная документация и команды запуска: [`start.txt`](start.txt).

> Учебная демонстрация: значения не являются секретами. Не возвращайте настоящие секретные cookies из API и не публикуйте секреты в `VITE_*`.
