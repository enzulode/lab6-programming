## Lab6
```Andreev Vladislav```
##### Task id: ```31271```

## Task
<a href="docs/task">Task screenshots available here</a>

## Description
This application is a client-server collection management system. This project is made in educational purposes.

## Building
To build executable ```jar``` files you have to do the following
```shell
./gradlew clean build
```

## Usage
First of all, you have to build the application (check the previous block)

### How to run ```server```
Add the ```SAVING_FILE``` environment variable
```shell
export SAVING_FILE=<whatever saving file you want>
```

And then run
```shell
java -jar server/build/libs/server-<version>.jar
```

### How to run ```client```
Just run the following
```shell
java -jar client/build/libs/client-<version>.jar
```
