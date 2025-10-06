# 🛍️ MicroShop — Микросервисное приложение для управления заказами

MicroShop — это демонстрационный микросервисный проект интернет-магазина, разработанный с использованием **Spring Boot**, **Spring Cloud**, **Docker**, **PostgreSQL** и **Keycloak**.  
Проект демонстрирует архитектуру распределённых сервисов с безопасной аутентификацией, взаимодействием через REST API и использованием централизованного управления пользователями.

---

## 🧩 Архитектура

Проект состоит из нескольких независимых микросервисов:

| Сервис | Назначение | Порт по умолчанию |
|--------|-------------|------------------|
| 🧾 **order-service** | Управление заказами, расчёт общей стоимости | `8082` |
| 📦 **product-service** | Управление товарами (CRUD) | `8081` |
| 💰 **payment-service** | Обработка платежей и статусов оплат | `8083` |
| 👤 **Keycloak** | Аутентификация и авторизация пользователей | `8080` |
| 🐘 **PostgreSQL** | Хранение данных всех микросервисов | `5432` |

Все сервисы контейнеризованы и управляются через **Docker Compose**.

---

## 🚀 Запуск проекта
1️⃣ Клонировать репозиторий
git clone https://github.com/Sifuentes00/microshop.git

cd microshop/microshop-parent

2️⃣ Собрать проект

mvn clean package -DskipTests

3️⃣ Запустить через Docker Compose

docker-compose up -d

## 🔐 Аутентификация и авторизация
Проект использует Keycloak как Identity Provider.
Realm: microshop
Клиенты:
order-service
product-service
payment-service
Пользователи создаются через Keycloak Admin Console.
Каждый запрос к защищённым endpoint’ам должен содержать:
Authorization: Bearer <JWT_TOKEN>


## 🧠 Основная логика
При создании заказа order-service обращается к product-service для получения данных о товарах.
Стоимость заказа вычисляется динамически на основе текущих цен товаров.
payment-service может обновлять статус оплаты через REST API.

## 🔗 Доступ к документации
После запуска контейнера Apache документация доступна по адресу:
http://localhost:8088/docs/

Файлы документации находятся в:
docs/source/

 ├── index.rst

 ├── quickstart.rst

 ├── api_reference.rst

 ├── product_service.rst

 ├── order_service.rst
 
 └── payment_service.rst

## 🛠️ Технологии

| Категория       | Используемые технологии                              |
|-----------------|-----------------------------------------------------|
| **Backend**     | Java 17, Spring Boot 3, Spring Data JPA, Spring Security |
| **Микросервисы**| Spring Cloud, REST, Docker                           |
| **Безопасность**| Keycloak, JWT OAuth2 Resource Server                |
| **База данных** | PostgreSQL                                         |
| **Инфраструктура** | Docker Compose                                   |
| **Документация** | Sphinx, Furo, reStructuredText                                  |
| **Инструменты сборки** | Maven                                         |