How to Run
=======
1. You must define your own application.properties file from the template file.
`cp src/main/resources/application.properties.template application.properties`
2. Uncomment the following lines and input your values
```properties
spring.datasource.url = jdbc:mysql://<your-db-host>:3306/club?useUnicode=true&characterEncoding=utf-8
spring.datasource.username = <your-db-user-name>
spring.datasource.password = <your-db-password>
app.host.address=<your-app-host-address> # http://www.example.com
#wechat.mp.appId= <your-appid>
#wechat.mp.secret= <your-secret>
#wechat.mp.token= <your-token>
#wechat.mp.aesKey= <your-aesKey>
```
3. Put your application.properties file under current directory or under Java class path
4. Then run your application
`java -jar target/education.jar`

Enjoy.
