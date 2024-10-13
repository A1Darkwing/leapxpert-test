# LeapXpert Assignment Test

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#Overview)

## ➤ Overview

This project is a common problem when we are doing with microservices. Recieved a request and make a external call to
3rd paties api.
But how to make our services scalable and dealing with various 3rds are usually makes us crazy.
And this repository contains the way how we design (and implemented) a system which can survival with a huge traffic and
various low 3rd parties.

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#Architecture)

## ➤ Architecture

Instead of using sync api when calling 3rd party servies, we will using async api to make our system scalable.
Unless our system will hang and crash when we recieved a hug traffic because we took a lot of memory when holding a
request that calling to 3rd services.

Our approach is simple. When we received a request, we put it into the queue (kafka) and generate a transactionId to
send it back to the `caller`.
A `worker` will consume this request from the queue and make a http request to call 3rd party. Then update the status
after they got a response from 3rd party.
Then `worker` will update transaction status to the cache and DB.

When a `caller` want to see the status of the transaction, they can make a request to query transaction status by id.
If our `worker` have finished process the request from caller, `caller` will see the result of transaction.
Unless, `caller` will see the Processing Status.

`caller` can have logic to query status for every second they see the Processing Status.
After a limitted time (ex: 30s), `caller` can stop query status and give some feedback to users.

With this approach, we can scale up our system easily. Increase worker instance (pods) to let our system process
requests from `caller` faster.
The api for query status is just hit our cache (redis) and it would be safety.
Both Service and Worker are designed to scale-up.

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#Installation)

## ➤ Installation

This project require MySQL, Redis, Kafka to start. So, please run the docker file before you start the application by
execute command below:

```
docker-compose up
```

Try to connect to mysql db and execute the query in `message-service/src/main/resources/init_db.sql`

After that, you can access project and start our application by execute these command

First, go to common lib and build this project

```
cd message-common
mvn clean install
```

Then, go back to parent folder and build project

```
cd ..
mvn clean install
```

Now you can start the Message Services

```
cd message-service
mvn spring-boot:run 
```

And start Message Worker

```
cd message-worker
mvn spring-boot:run
```

After Message Services start successfully, you can visit Swagger and make some request to test our application

```
http://localhost:8079/swagger-ui.html#/
```

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#Use_Cases)

## ➤ Load-test

Need to create more partition when running loadtest. This is for scale-out testing. We can start 2-3 worker to see how
they improve performance

### Create Chat Room

Need to install k6 to do performance testing

```
k6 run .\loadtest-create-group.js
```

Our testing result is about ~1918 TPS with P(95)=9ms

### Import Message

Need to install k6 to do performance testing

```
k6 run .\loadtest-import-message.js
```

Our testing result is about ~1223 TPS with P(95)=71ms

### Export Message

Use swagger or direct url

```
http://localhost:8079/messages/export?endDate=1738815215&startDate=1728813667
```

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#deployment_model)

## ➤ Technical Stack

### Application

- Spring Boot
- JDBC Template
- Swagger
- Http Client

### Cache

- Redis

### Queue

- Kafka

### DB

- Mysql

### Monitoring

- Prometheus
- Grafana

### Benchmark

- K6

[![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/colored.png)](#project_structure)

## ➤ Project Structure

The same structure with other spring boot project except:

- `externalapi` folder: each 3rd parties should be one folder.
  In this example, `thirdpartiessystem` folder should be an external system.
  They may provide us more than one api. Each api should be a folder (ex: user service api should be a usersystem
  folder).
  And we can store Request/Response object in this folder, included `Mapping` Config.