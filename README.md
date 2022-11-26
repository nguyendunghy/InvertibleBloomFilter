# Ibf aggregation test


# How to test 
- Start oracle database
- Run project by run main method at StatrysTestingApplication.java
- Call postman API:
+ Init
  `
  curl --location --request GET 'localhost:8080/ibf/ibf/init' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'`
  
+ Diff
`
  curl --location --request GET 'localhost:8080/ibf/ibf/diff' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'
`
+ Retrieve data
`
  curl --location --request GET 'localhost:8080/ibf/ibf/retrieve/207683692056764796' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'
`