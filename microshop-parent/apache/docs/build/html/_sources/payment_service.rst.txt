=========================
Payment Service — API
=========================

Сервис оплаты отвечает за обработку, получение и удаление платежей, связанных с заказами.  
Он взаимодействует с заказами (Order Service) и обеспечивает базовую проверку прав доступа через Keycloak.

----

Общие сведения
===============

- **Базовый URL:** ``/api/payments``
- **Авторизация:** требуется (JWT через Keycloak)
- **Роли:**
  - ``ROLE_USER`` — может выполнять оплату и просматривать свои платежи
  - ``ROLE_ADMIN`` — может просматривать и удалять все платежи

----

Получить список всех платежей
==============================

**GET** ``/api/payments``  
**Доступ:** ROLE_ADMIN  

Возвращает список всех платежей в системе (для администратора).

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/payments \
     -H "Authorization: Bearer <admin-token>"

**Пример ответа:**

.. code-block:: json

   [
     {
       "id": 1,
       "orderId": 42,
       "amount": 1599.98,
       "method": "CARD",
       "status": "SUCCESS",
       "createdAt": "2025-10-06T14:25:00Z",
       "userId": "user123"
     }
   ]

**Коды ответа:**
- ``200 OK`` — успешно

----

Получить платёж по ID
======================

**GET** ``/api/payments/{id}``  
**Доступ:** ROLE_USER  

Возвращает информацию о платеже по его идентификатору.

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/payments/1 \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   {
     "id": 1,
     "orderId": 42,
     "amount": 1599.98,
     "method": "CARD",
     "status": "SUCCESS",
     "createdAt": "2025-10-06T14:25:00Z",
     "userId": "user123"
   }

**Коды ответа:**
- ``200 OK`` — найден
- ``404 Not Found`` — не найден

----

Совершить оплату
================

**POST** ``/api/payments/pay``  
**Доступ:** ROLE_USER  

Позволяет пользователю оплатить заказ. Пользователь определяется по JWT-токену.

**Тело запроса:**

.. code-block:: json

   {
     "orderId": 42,
     "amount": 1599.98,
     "method": "CARD"
   }

**Пример ответа:**

.. code-block:: json

   {
     "id": 5,
     "orderId": 42,
     "amount": 1599.98,
     "method": "CARD",
     "status": "SUCCESS",
     "createdAt": "2025-10-06T14:28:00Z",
     "userId": "user123"
   }

**Коды ответа:**
- ``200 OK`` — оплата прошла успешно
- ``400 Bad Request`` — неверные данные
- ``401 Unauthorized`` — токен не передан или недействителен

----

Удаление платежа
=================

**DELETE** ``/api/payments/{id}``  
**Доступ:** ROLE_ADMIN  

Удаляет платёж по идентификатору.

**Пример запроса:**

.. code-block:: bash

   curl -X DELETE http://localhost:8080/api/payments/5 \
     -H "Authorization: Bearer <admin-token>"

**Коды ответа:**
- ``204 No Content`` — удалено успешно
- ``404 Not Found`` — платёж не найден

----

Модель данных
==============

**PaymentDto**

.. code-block:: json

   {
     "orderId": 42,
     "amount": 1599.98,
     "method": "CARD"
   }

**Поля:**

- ``orderId`` — идентификатор заказа (Long)
- ``amount`` — сумма платежа (BigDecimal)
- ``method`` — способ оплаты (например: `"CARD"`, `"CASH"`, `"TRANSFER"`)

----

**Payment (Entity)**

.. code-block:: json

   {
     "id": 1,
     "orderId": 42,
     "amount": 1599.98,
     "method": "CARD",
     "status": "SUCCESS",
     "createdAt": "2025-10-06T14:25:00Z",
     "userId": "user123"
   }

**Поля:**

- ``id`` — идентификатор платежа  
- ``orderId`` — ID связанного заказа  
- ``amount`` — сумма платежа  
- ``method`` — способ оплаты  
- ``status`` — состояние (например, SUCCESS, FAILED, PENDING)  
- ``createdAt`` — время создания  
- ``userId`` — ID пользователя, совершившего оплату  

