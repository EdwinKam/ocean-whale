# ocean-whale

this is the backend service of Ocean

# Requirement

java 17

# How to start

1. set up database secret locally

- ask app owner for the secret, put the secret in the root folder
https://drive.google.com/file/d/1SUt6lzcDyjZwdrczvbHU4tXa42jtatqV/view?usp=sharing
- [not recommended] Go to firebase console -> project settings -> service accounts -> generate new private key, make sure the json name matches what inside `FirebaseConfig.java`, not sure if everyone else will need to use the new secret

Open intellij, just run the main function in WhaleApplication.java

# How to test

open browser http://localhost:8080/api/hello. You should see hello world if the server is running properly.
