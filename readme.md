How to Run
=======
1. You must define your own application.properties file from the template file.
```sh
cp src/main/resources/application.properties.template application.properties
```
2. Uncomment the following lines and input your values
```properties
spring.datasource.url = jdbc:mysql://<your-db-host>:3306/club?useUnicode=true&characterEncoding=utf-8
spring.datasource.username = <your-db-user-name>
spring.datasource.password = <your-db-password>

wechat.mp.appId= <your-appid>
wechat.mp.secret= <your-secret>
wechat.mp.token= <your-token>
wechat.mp.aesKey= <your-aesKey>
```
3. Put your application.properties file under current directory or under Java class path
4. Then run your application
`java -jar target/education.jar`

Weixin Media Platform Configuration
=======
1. Logon to your media platform account:
[https://mp.weixin.qq.com/](https://mp.weixin.qq.com/)
2. Edit your "接口配置信息"
```
URL = http://<server-url>/wechat/portal
```
3. Edit "网页授权获取用户基本信息", add <server-url> to the whitelist
4. Copy and edit `/src/main/resources/wechat/menu.json`
```json
    "sub_button":[
    {   
      "type":"view",
      "name":"...",
      "url":"http://<server-url>/user/center"
    }

```
5. Create your menu from menu.json

Enjoy.
