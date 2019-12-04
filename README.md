# Sample Java Project

This project server as a sample project.

* It shows how to integrate a java project with the deployment pipeline
* I also provides with a [docker compose](docker/docker-compose.yml) file which allows to set up a complete toolchain locally on you machine.
* There is a [documentation](doc/adoc.adoc) about the toolchain setup. 

## User env

Docker compose will create the infrastructure, the default urls and username password are listed below.
### Jenkins  
[http://127.0.0.1:8080](http://127.0.0.1:8080)
User: admin
Password: password

### Artifactory
[http://127.0.0.1:8081](http://127.0.0.1:8081)
User: admin
Password: password

### BitBucket
[http://127.0.1:7990](http://127.0.1:7990)
User: admin
Password: password

### SonarQube
[http://127.0.1:9000](http://127.0.1:9000)
User: admin
Password: admin