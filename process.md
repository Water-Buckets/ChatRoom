# chatting
1. client1: Establish https connection. POST using https. 
2. sever: receive, [store it into the database using saveMessage()](src/main/java/com/waterbucket/chatroom/service/MessageService.java) and POST if user(receiver) is online(connection established).
3. client2: GET messages when login. When logged-in(connection established) do nothing but wait for the server to post the message.
4. server: Use [getMessagesByRoomId()](src/main/java/com/waterbucket/chatroom/controller/ChatController.java) and post them.

# creating user
...

# deleting user
...

