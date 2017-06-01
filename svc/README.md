# Act-Chatroom Demo - Backend Service

## 1. Introduction

Act-Chatroom demo application is used to showcase how to play with WebSocket facilities provided by ActFramework (Since 1.4.0). The entire demo project is created as separated front-end and backend applications.

The backend service is the backend part of the demo project, and it is the hard core of the demonstration about ActFramework websocket APIs.

## 2. Requirements

### 2.1 General requirements
* All services shall be provided as RESTFul/JSON style
* All service endpoints shall come up with URL context `/api/v1`
* It shall support CORS as the front end app might be hosted on a different domain name


### 2.2 Login/Logout

* It shall allow the user to login through social platform. At the moment the following social platform are supported:
   * Facebook
   * LinkedIn
   * GitHub
   
* It shall allow the user to logout the app

### 2.3 Room services

* Any user can chat in the main room
* Logged in user can chat in other rooms (created by administrator)

### 2.4 Administration support

* It shall provides CLI command for administrator to create new chat rooms

## 3. Implementation

### 3.1 Data structures

#### 3.1.1 User

The user data is returned when front-end query for user list of a certain room

```json
{
  "email": "<email>",
  "screenname": "<display name>"
}
```

#### 3.1.2 Room

The room data is returned when front-end query for room list

```json
{
  "name": "<room name>",
  "desc": "<room description>"
}
```

#### 3.1.3 Message

The message data should be send by front end to the server through websocket when a user entered a piece of message in a chat room

```json
{
  "room": "<room name>",
  "text": "<the text message>"
}
```

### 3.2 API

#### 3.2.1 Login Services

```
# Login through social channel
TBD

# Login out
POST /api/v1/logout
```

#### 3.2.2 Room Services

```
# get all rooms
GET /api/v1/rooms

# get all users who joined a room
GET /api/v1/rooms/{roomName}/users

# join the current user into a room
POST /api/v1/rooms/{roomName}

# quit the current user from a room
DELETE /api/v1/rooms/{roomName}
```

#### 3.2.3 Chat service 

```
# The websocket connection endpoint
GET /api/v1/chat
```