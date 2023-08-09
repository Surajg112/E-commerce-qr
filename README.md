# FullStackRetail

Ecommerce Backend
Software Required
Java 17
MySQL 8 & SQLYog
MongoDB 6 & MongoDB Compass
IntelliJ Idea
ZipKin Server

Create new folder- Ex- RetailEcommerce

Steps To Clone https://github.com/jadhavkirangreatlearning/FullStackRetail.git Repository To Local

Steps To Start Projects
Open all projects in IntelliJ Idea

Build the all projects using maven :- mvn clean install

First run ServiceRegistry

Then run Wishlistservice , OrderService , CartService , ApiGateway , UserService

Note: If you PostgreSQL in your system then change spring profile test to dev in application.yml for CartService & WishlistService and create database accordingly

For ProductService please open this project in new window

Note: If you open in same window for save & update product functionality it give 404 exception - File Not Found

Please download Zipkin Server from here. Paste the zipkin-server-2.23.19-exec file in ECommerceApp-Backend folder. Open cmd from ECommerceApp-Backend location type java -jar zipkin-server-2.23.19-exec enter

Important URL'S
Service Registry Port - 8761 :- Eureka Server

API Gateway Port - 9000 :-

Note: For Gateway API Documentation Please Use Postman

User Service Port - 9001 :- API Documentation For User Service

Product Service Port - 9002 :- API Documentation For Product Service

Cart Service Port - 9003 :- API Documentation For Cart Service

Order Service Port - 9004 :- API Documentation For Order Service

Wishlist Service Port - 9005 :- API Documentation For Wishlist Service

Hystrix Dashboard Port - 9009 :- Hystrix Dashboard

Zipkin Server Port - 9411 :- Zipkin Server

Admin Server Port - 8761 :- Spring Boot Admin Server



Front End-
Software Required
Node JS
VS Code
Steps To Start Project
Open this project in VS Code

Open new terminal shortcut key -

Ctrl + `
and type npm install + press Enter

To start application type npm start + press Enter to start project

Note: Please note that server running on http://localhost:8000

Steps To Creating Admin User
First do Signup

After successfull sign up please open MongoDB Compass -> open connection -> open UserService Database -> open UserInfo -> double click on "USER" -> change it into ADMIN
(it looks like in db=> roles: "ADMIN") -> click on update buttton

Signin using this details

Note: Please note that UserName is your EmailId

Steps To Add New Product
Signin using Admin user
Click on Dashboard -> Click Manage Category -> Add new category -> save
Click on Dashboard -> Click Manage Sub Category -> Add new subcategory -> save
Click on Dashboard -> Click Manage Product -> Add product -> save
For Creating New Coupon
Signin using admin user
Click on Dashboard -> Manage Coupon -> Add Coupon -> save
Create New User
Sign Up
Sign In
