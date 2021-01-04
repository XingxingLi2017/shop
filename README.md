# Shop
A distributed backend system for online shopping mall website using microservice architecture with Spring Cloud and SpringBoot.  
Mainly includes 9 modules: 
  * Commodity information management
  * Ads content management
  * Order management
  * User information management
  * Limited-time offer
  * Commodity information search
  * Payment
  * Oauth 2.0 user authentication and authorization
  * Distributed file storage
# System Architecture Diagram
![image](https://github.com/XingxingLi2017/shop/blob/master/project-image/ShopArchitecture.PNG)
# Technology Stack
  ## Deployment and maintainence
    * VMWare and CentOS 7
    * Maven
    * Docker and docker compose
    * MySql and Canal
    * FastDFS
    * Redis
    * Elasticsearch and Kibana
    * OpenResty and Lua
    * RabbitMQ
    * Seata Server
  ## Web Frontend
    * Thymeleaf
    * Vue.js
    * Node.js
    * Lua
    * Html5
    * ElementUI
  ## Microservice
    * SpringBoot
    * Spring Security Oauth2.0
    * JWT
    * Spring AMQP
    * Spring Cloud
      ** Spring Cloud Eureka
      ** Spring Cloud Gateway
      ** Spring Cloud Bus
      ** Spring Cloud Config
      ** Spring Cloud OpenFeign
      ** Spring Cloud Hystrix
      ** Spring Cloud Task
  ## Data persistence
    * Mybatis and TK Mybatis
    * Spring Data Elasticsearch
    * Spring Data Redis
  ## Database and message queue
    * MySql
    * RabbitMQ
  ## External interface
    * Wechat payment
