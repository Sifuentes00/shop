=========================
Order Service — API
=========================

Сервис заказов отвечает за создание, просмотр, обновление и удаление заказов пользователей.  
Обрабатывает связи с товарами и поддерживает изменение статуса.

----

Общие сведения
===============

- **Базовый URL:** ``/api/orders``
- **Авторизация:** требуется (JWT через Keycloak)
- **Роли:**
  - ``ROLE_USER`` — доступ на все операции с заказами своего пользователя
  - ``ROLE_ADMIN`` — (при необходимости) может управлять всеми заказами

----

Список всех заказов
===================

**GET** ``/api/orders``  
**Доступ:** ROLE_USER  

Возвращает список всех заказов текущего пользователя.

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/orders \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   [
     {
       "id": 1,
       "userId": "user123",
       "status": "CREATED",
       "totalAmount": 1599.98,
       "createdAt": "2025-10-06T14:20:00Z",
       "updatedAt": "2025-10-06T14:20:00Z",
       "items": [
         {
           "productId": 1,
           "quantity": 2,
           "price": 799.99
         }
       ]
     }
   ]

----

Получение заказа по ID
======================

**GET** ``/api/orders/{id}``  
**Доступ:** ROLE_USER  

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/orders/1 \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   {
     "id": 1,
     "userId": "user123",
     "status": "CREATED",
     "totalAmount": 1599.98,
     "createdAt": "2025-10-06T14:20:00Z",
     "updatedAt": "2025-10-06T14:20:00Z",
     "items": [
       {
         "productId": 1,
         "quantity": 2,
         "price": 799.99
       }
     ]
   }

**Коды ответа:**
- ``200 OK`` — заказ найден
- ``404 Not Found`` — заказ не найден

----

Создание нового заказа
=======================

**POST** ``/api/orders``  
**Доступ:** ROLE_USER  

**Тело запроса:**

.. code-block:: json

   {
     "userId": "user123",
     "items": [
       {
         "productId": 1,
         "quantity": 2,
         "price": 799.99
       }
     ],
     "totalAmount": 1599.98
   }

**Пример ответа:**

.. code-block:: json

   {
     "id": 2,
     "userId": "user123",
     "status": "CREATED",
     "totalAmount": 1599.98,
     "createdAt": "2025-10-06T14:22:00Z",
     "updatedAt": "2025-10-06T14:22:00Z",
     "items": [
       {
         "productId": 1,
         "quantity": 2,
         "price": 799.99
       }
     ]
   }

**Код ответа:** ``201 Created``

----

Удаление заказа
================

**DELETE** ``/api/orders/{id}``  
**Доступ:** ROLE_USER  

Удаляет заказ по идентификатору.

**Пример запроса:**

.. code-block:: bash

   curl -X DELETE http://localhost:8080/api/orders/1 \
     -H "Authorization: Bearer <token>"

**Код ответа:** ``204 No Content``

----

Обновление статуса заказа
==========================

**PUT** ``/api/orders/{id}/status?status={newStatus}``  
**Доступ:** ROLE_USER или ROLE_ADMIN  

**Пример запроса:**

.. code-block:: bash

   curl -X PUT "http://localhost:8080/api/orders/1/status?status=SHIPPED" \
     -H "Authorization: Bearer <token>"

**Пример ответа:**  
(нет тела)

**Коды ответа:**
- ``200 OK`` — статус обновлён
- ``404 Not Found`` — заказ не найден

----

Модель данных
==============

**OrderDto**

.. code-block:: json

   {
     "id": 1,
     "userId": "string",
     "status": "CREATED",
     "totalAmount": 0.00,
     "createdAt": "2025-10-06T14:00:00Z",
     "updatedAt": "2025-10-06T14:00:00Z",
     "items": [
       {
         "productId": 1,
         "quantity": 1,
         "price": 100.00
       }
     ]
   }

**Поля:**

- ``id`` — идентификатор заказа (Long)
- ``userId`` — ID пользователя в Keycloak
- ``status`` — статус заказа (строка, например: CREATED, PAID, SHIPPED)
- ``totalAmount`` — общая сумма (BigDecimal)
- ``createdAt`` / ``updatedAt`` — временные метки (Instant)
- ``items`` — список позиций заказа (см. ``OrderItemDto``)
