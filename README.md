# Teste Eicon

## Installing / Getting started

In order to compile the eicon Service run this command within the CLI.

```shell
mvnw clean install
mvnw spring-boot:run
```

## Requirement

### MySQL Database connection

In order to create a database a MySQL 8.x installation is required. After that, open a terminal (command prompt in Microsoft Windows) and open a MySQL client as a user who can create new users. For example, use the following command:

```shell
mysql -u root -p
```

This connects to MySQL as ```root``` and allows access to the user from all hosts. This is __not the recommended way__ for a production server.

To create a new database, run the following commands at the ```mysql``` prompt:

```shell
mysql> create user if not exists 'eicon'@'%' identified by 'eicon#2024'; -- Creates the user if not exists
mysql> create database teste_eicon; -- Creates the new database if not exists
mysql> grant all on teste_eicon.* to 'eicon'@'%'; -- Gives all privileges to the new user on the newly created database
```

## Developing

In order to start developing to the project further:

```shell
git clone git@github.com/iarlenaquiles/eicon.git
cd eicon/
mvnw clean install
mvnw spring-boot:run
```

### Deploying / Publishing

In order to build and run the project

```shell
mvnw clean package spring-boot:repackage
java -jar target/teste_eicon-[VERSION].jar
```
