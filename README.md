# ocean-whale

this is the backend service of Ocean

# Requirement

java 17

# How to start

1. set up database secret locally

- ask app owner for the secret, put the secret in the root folder
1. https://drive.google.com/file/d/1SUt6lzcDyjZwdrczvbHU4tXa42jtatqV/view?usp=sharing
- [not recommended] Go to firebase console -> project settings -> service accounts -> generate new private key, make sure the json name matches what inside `FirebaseConfig.java`, not sure if everyone else will need to use the new secret
2. https://drive.google.com/file/d/1h4Qb3oPBk0hX_hXCogrf38ioOmFja_uI/view?usp=sharing, this is application.properties
Open intellij, just run the main function in WhaleApplication.java

# How to test

open browser http://localhost:8080/api/hello. You should see hello world if the server is running properly.

# Swagger
http://localhost:8080/swagger-ui/index.html
https://ocean-whale.onrender.com/swagger-ui/index.html
