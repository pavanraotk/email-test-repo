# Email Repo

This project sends email through through sendgrid provider. There's an attached postman collection that helps in testing the application.

### Technologies used

* Spring-boot
* Docker
* Maven

### Prerequisite before building
Ensure maven is installed locallly

### Building the project
Run command ```mvn clean install```

### Docker image build
Once the application is built, build the docker image using the command ```docker build ./ -t email-service-app```
After building the image, run the application using command ```docker-compose up```

### Improvements to be made

* Metric the response time, metric the email sending time --> send it to grafana.
* Push the logs to ELK or ELK equivalent.
* Persist the emails in a data store to enable re-try if in case of a failure.

### Functionality not built
* Query param part with getting quote of the day.