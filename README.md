# taas
Time as a Service: get the current time in any timezone

##Description

Get the current hours, minutes, and seconds in JSON. 
By default, this will be in the timezone of the server, but you can optionally pass a <code>?timezone=<b>:timezone</b></code> query parameter to use a [different timezone](http://www.joda.org/joda-time/timezones.html).

##HTTP API example usage

```http
GET /taas/now

HTTP/1.1 200 OK
{ "hours": 17, "minutes": 31, "seconds": 35 }
```
```http
GET /taas/now?timezone=Europe/London

HTTP/1.1 200 OK
{ "hours": 1, "minutes": 31, "seconds": 35 }
```

##Deployment

###Prerequisites
- JDK ≥ 7
- Maven ≥ 3
- Servlet container like Jetty ≥ 8 or Tomcat ≥ 7

###Compilation
```bash
$ git clone https://github.com/Aldaviva/taas.git
$ cd taas
$ mvn package
```

###Execution
1. Copy `target/taas.war` to your servlet container's webapps directory, or however your server runs WARs.
2. Start your server.
3. Go to `http://127.0.0.1:8080/taas/now` or whatever your server's address and port are, and you should see a JSON response.
