<h1 align="center"># Shop Style :department_store: :shirt:</h1>

## This project was developed as a challenge on the Spring Fast Track training at Compass UOL.

### Description
Shop Style is a fictional physical clothing store of all styles. The project was developed with Java 17 and consists of five microservices:
- Customer
- Catalog
- Checkout
- History
- BFF-Shop

### Customer
MS Customer is responsible for storing and managing user data. It has the follow endpoints:
- POST /v1/login
- POST /v1/users
- GET /v1/users/:id
- PUT /v1 users/:id

### Catalog
MS catalog is responsible for storing the products and categories that will be
available on the application. A product has one or more variations, the variations have the
attributes color, size, price and quantity. The product is also linked to one
or more categories and a category can have zero or more products. It has the follow endpoints:
#### Products
- POST /v1/products
- GET /v1/products
- GET /v1/products/:id
- PUT /v1/products/:id
- DELETE /v1/products/:id
#### Variations
- POST /v1/variations
- PUT /v1/variations/:id
- DELETE /v1/variations/:id 
#### Categories
- POST /v1/categories
- GET /v1/categories
- GET /v1/categories/:id/products
- PUT /v1/categories/:id
- DELETE /v1/categories/:id

### Checkout
MS Checkout is responsible for storing the payment methods, discounts that can be applied and the purchase of products. It has the follow endpoints:
#### Payments
- POST /v1/payments
- GET /v1/payments
- GET /v1/payments/:id
- PUT /v1/payments/:id
- DELETE /v1/payments/:id
#### Purchases
- POST /v1/purchases

### History
MS History is responsible for storing all purchase history performed. It has the follow endpoints:
- GET /v1/historic/user/:idUser

### BFF-Shop
All micro-services will be for internal use by the company's employees. BFF is responsible for creating an entrypoint in which customer can communicate with the features.
It has the following entrypoints:
- POST /v1/users
- POST /v1/login
- GET /v1/users/:id
- PUT /v1/users/:id
- GET /v1/products/:id
- GET /v1/categories/:id/products
- GET /v1/payments
- POST /v1/purchases
- GET /v1/historic/user/:idUser
