# RoomOccupancyTool
To compile and build application please use the following commands:
mvn compile
mvn package
You can do it by yourself or using already provided jar file. To run application please type:
java -jar RoomOccupancyTool-0.0.1-SNAPSHOT.jar

By default application runs on http://localhost:8080/
First window will be a login page:
![image](https://github.com/kmadej/RoomOccupancyTool/assets/9861925/0ede927d-54ba-42cd-a8a6-e354e48bcaff)

Default test credentials are:
username: user,
password: user

There is only one GET endpoint (/roomOccupancy) to get room optimalization tool result providing the following input:
int freePremiumRooms,
int freeEconomyRooms,
int[] potentialGuests

Example request: http://localhost:8080/roomOccupancy?freePremiumRooms=5&freeEconomyRooms=6&potentialGuests=100,90,250
Example response: {"usagePremium":2,"usageEconomy":1,"revenuePremium":350,"revenueEconomy":90}

Of course, you can use postman to send request. Application use basic authentication.
