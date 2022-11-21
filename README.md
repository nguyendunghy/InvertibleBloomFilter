# Ibf aggregation test


# How to test 
- Start oracle database
- Run project by run main method at StatrysTestingApplication.java
- Call postman API:
  `
  curl --location --request GET 'localhost:8080/ibf/ibf/runIbf' \
--header 'Content-Type: application/json' \
--data-raw '{}'`
