# train-ticketing-system

Train Ticketing System RESTful API provides endpoints for purchasing train tickets, managing user details, and retrieving ticket information.

Getting Started
To get started with this project, follow these steps:

1. Clone the repository: git clone https://github.com/Dharmashivam/train-ticketing-system.git
2. Open the project in IDE
3. Import the project into your IDE as a Maven project
4. Run the TrainTicketingSystemApplication.java class in your IDE to start the application
5. Open PostMan tool to test below api requests.

Endpoints<br>
Purchase Ticket<br>
Endpoint: POST /tickets<br>
Description: Purchase a train ticket from London to France.<br>
Request Body:<br>
                {
                    "user": {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johns.doe@example.com"
                    },
                    "train": {
                        "trainId": 2,
                        "fromLocation": "Chennai",
                        "toLocation": "Mumbai"
                    },
                    "pricePaid": 50.00,
                    "sectionPreference": "B"
                }<br>
Response Body:<br>
                {
                    "user": {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johns.doe@example.com"
                    },
                    "train": {
                        "trainId": 2,
                        "fromLocation": "Chennai",
                        "toLocation": "Mumbai"
                    },
                    "pricePaid": 50.0,
                    "sectionPreference": "B",
                    "seatNumber": 3,
                    "id": 6
                }<br>

Get Ticket Details<br>

Endpoint: GET /tickets/{ticketId}<br>
Description: Get details of a specific ticket by its ID.<br>
Path Parameter: ticketId - ID of the ticket to retrieve.<br>
Response: Details of the ticket including from, to, user, and price paid.<br>
                {
                    "user": {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johns.doe@example.com"
                    },
                    "train": {
                        "trainId": 2,
                        "fromLocation": "Chennai",
                        "toLocation": "Mumbai"
                    },
                    "pricePaid": 50.0,
                    "sectionPreference": "A",
                    "seatNumber": 1,
                    "id": 1
                }<br>

View Users and Seats by Section<br>

Endpoint: GET /tickets/users-and-seats/{trainId}/{section}<br>
Description: View users and their allocated seats by the requested section (A or B).<br>
Path Parameters:<br>
trainId: ID of the train.<br>
section: Section of the train (A or B).<br>
Response: List of users with their allocated seats in the requested section.<br>
                [
                    {
                        "user": {
                            "id": 1,
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "johns.doe@example.com"
                        },
                        "seatNumber": 1
                    },
                    {
                        "user": {
                            "id": 1,
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "johns.doe@example.com"
                        },
                        "seatNumber": 2
                    },
                    {
                        "user": {
                            "id": 1,
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "johns.doe@example.com"
                        },
                        "seatNumber": 3
                    }
                ]<br>
Remove User from Train<br>
<br>
Endpoint: DELETE /users/{userId}<br>
Description: Remove a user from the train.<br>
Path Parameter:<br>
userId: ID of the user to remove.<br>
Response: Indicates whether the user was successfully removed.<br>
User with ID 2 has been successfully removed, and associated tickets have been canceled.<br>

Modify User's Seat<br>

Endpoint: PUT /tickets/{ticketId}?userId={userId}<br>
Description: Modify a user's seat in the train.<br>
Path Parameters:<br>
ticketId: ID of the ticket to modify.<br>
Query Parameters:<br>
userId: ID of the user requesting the modification.<br>
before change seat:<br>
                {
                    "user": {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johns.doe@example.com"
                    },
                    "train": {
                        "trainId": 2,
                        "fromLocation": "Chennai",
                        "toLocation": "Mumbai"
                    },
                    "pricePaid": 50.0,
                    "sectionPreference": "A",
                    "seatNumber": 1,
                    "id": 1
                }<br>
http://localhost:8080/train-ticketing-system/tickets/1?userId=1<br>
Response Body:<br>
                {
                    "user": {
                        "id": 1,
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "johns.doe@example.com"
                    },
                    "train": {
                        "trainId": 2,
                        "fromLocation": "Chennai",
                        "toLocation": "Mumbai"
                    },
                    "pricePaid": 50.0,
                    "sectionPreference": "B",
                    "seatNumber": 1,
                    "id": 1
                }<br>

Assumptions:-<br>
User Details: Each ticket purchase requires providing the user's first name, last name, and email address.<br>
Train Sections: The train has two sections: A and B.<br>
Seat Allocation: Seats are allocated automatically based on availability in the selected section.<br>
Cancellation and Modification: Users can cancel their ticket or modify their seat within certain constraints.<br>


Known Limitations:-<br>
Authentication and Authorization: This API does not implement authentication and authorization mechanisms.<br>
Validation: Basic validation is implemented, but more sophisticated validation could be added.<br>
Concurrency: The API may encounter issues with concurrent ticket purchases or modifications.<br>
Performance: No optimizations are made for performance, which may impact scalability under heavy loads.<br>
Error Handling: While basic error handling is implemented, more comprehensive error handling could be added for various scenarios.<br>

