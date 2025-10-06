=========================
Product Service — API
=========================

Сервис отвечает за управление товарами в системе Microshop.  
Поддерживает операции CRUD, поиск и получение цены.

----

Общие сведения
===============

- **Базовый URL:** ``/api/products``
- **Авторизация:** требуется (JWT через Keycloak)
- **Роли:**
  - ``ROLE_USER`` — доступ на чтение
  - ``ROLE_ADMIN`` — доступ на создание, обновление и удаление

----

Список всех товаров
===================

**GET** ``/api/products``  
**Доступ:** ROLE_USER

Возвращает список всех товаров в виде JSON.

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/products \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   [
     {
       "id": 1,
       "name": "Laptop",
       "description": "High-performance laptop",
       "price": 999.99,
       "stockQuantity": 15
     },
     ...
   ]

----

Получение одного товара
========================

**GET** ``/api/products/{id}``  
**Доступ:** ROLE_USER

**Пример запроса:**

.. code-block:: bash

   curl -X GET http://localhost:8080/api/products/1 \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   {
     "id": 1,
     "name": "Laptop",
     "description": "High-performance laptop",
     "price": 999.99,
     "stockQuantity": 15
   }

----

Получение цены товара
======================

**GET** ``/api/products/{id}/price``  
**Доступ:** ROLE_USER

**Пример ответа:**

.. code-block:: json

   999.99

----

Поиск товаров по названию
==========================

**GET** ``/api/products/search?name={query}``  
**Доступ:** ROLE_USER

**Пример запроса:**

.. code-block:: bash

   curl -X GET "http://localhost:8080/api/products/search?name=phone" \
     -H "Authorization: Bearer <token>"

**Пример ответа:**

.. code-block:: json

   [
     {
       "id": 2,
       "name": "Smartphone",
       "description": "Android smartphone",
       "price": 599.99,
       "stockQuantity": 50
     }
   ]

----

Создание товара
================

**POST** ``/api/products``  
**Доступ:** ROLE_ADMIN  
**Тело запроса:**

.. code-block:: json

   {
     "name": "New Product",
     "description": "Product description",
     "price": 199.99,
     "stockQuantity": 30
   }

**Пример ответа:**

.. code-block:: json

   {
     "id": 5,
     "name": "New Product",
     "description": "Product description",
     "price": 199.99,
     "stockQuantity": 30
   }

**Код ответа:** ``201 Created``  
**Заголовок Location:** ``/api/products/{id}``

----

Обновление товара
==================

**PUT** ``/api/products/{id}``  
**Доступ:** ROLE_ADMIN

**Тело запроса:**

.. code-block:: json

   {
     "name": "Updated Laptop",
     "description": "Improved performance",
     "price": 1099.99,
     "stockQuantity": 10
   }

**Пример ответа:**

.. code-block:: json

   {
     "id": 1,
     "name": "Updated Laptop",
     "description": "Improved performance",
     "price": 1099.99,
     "stockQuantity": 10
   }

----

Удаление товара
================

**DELETE** ``/api/products/{id}``  
**Доступ:** ROLE_ADMIN

**Пример запроса:**

.. code-block:: bash

   curl -X DELETE http://localhost:8080/api/products/1 \
     -H "Authorization: Bearer <token>"

**Код ответа:** ``204 No Content``

----

Модель данных
==============

**ProductDto**

.. code-block:: json

   {
     "id": 1,
     "name": "string",
     "description": "string",
     "price": 0.00,
     "stockQuantity": 0
   }

**Поля:**

- ``id`` — идентификатор товара (Long)
- ``name`` — название (строка, обязательно)
- ``description`` — описание (строка, опционально)
- ``price`` — цена (BigDecimal, ≥ 0)
- ``stockQuantity`` — количество на складе (целое, ≥ 0)
