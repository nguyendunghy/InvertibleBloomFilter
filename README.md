# Ibf aggregation test



# How to test API
1. Start oracle database 
2. Run all query directory resources/sql
3. Run project by run main method at InvertibleBloomFilterApplication.java 
4. Run **Init api** to init project:<br>
  `
  curl --location --request GET 'localhost:8080/ibf/ibf/init' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'`
  
- ### UPSERT
1. Add a new record in table IBF_DATA
2. Call API to see hash value:<br>
`
  curl --location --request GET 'localhost:8080/ibf/ibf/diff' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'
`
3. Retrieve new added record: <br>
`
  curl --location --request GET 'localhost:8080/ibf/ibf/retrieve/207683692056764796' \
  --header 'Content-Type: application/json' \
  --data-raw '{}'
`
- ### DELETE
1. Create table IBF_DATA_HISTORY by query:<br>
   ```CREATE TABLE IBF_DATA_HISTORY as SELECT * FROM IBF_DATA;```
2. Delete a record in table IBF_DATA
3. Call API to see hash value:<br>
`
curl --location --request GET 'localhost:8080/ibf/ibf/diff' \
--header 'Content-Type: application/json' \
--data-raw '{}'
`
4. Retrieve deleted record: <br>
`
   curl --location --request GET 'localhost:8080/ibf/ibf/retrieve/history/204961177546290452' \
   --header 'Content-Type: application/json' \
   --data-raw ''
`

# How to test API IbfCheckpointManagerTest.diff
- ### Init test
1. Start oracle database
2. Run init.sql
3. Run method: IbfCheckpointManagerTest.testDiff and find UPSERT, DELETE in logs to see the results


- ### Test UPSERT
1. Insert a new record to table IBF_DATA
2. Run method: IbfCheckpointManagerTest.testDiff and find UPSERT in logs to see the results

- ### Test DELETE
1. Create a new table IBF_DATA by query: <br>
```CREATE TABLE IBF_DATA_HISTORY as SELECT * FROM IBF_DATA;```
2. Delete a record to table IBF_DATA
3. Run method: IbfCheckpointManagerTest.testDiff and find DELETE in logs to see the results