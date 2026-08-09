# Java-RMI-Chat
Simple Java RMI chat application

# Java RMI Chat

A multi-client chat application developed in Java using **Java RMI (Remote Method Invocation)**.

The application follows a client-server architecture and allows multiple clients to connect to a central server and exchange messages. Both the client and server include graphical user interfaces.

> **Academic project:** developed as a team assignment during university studies.

## Features

* Client-server communication using Java RMI
* Support for multiple connected clients
* Graphical user interface for the client
* Graphical user interface for the server
* Sending and receiving chat messages
* Displaying the list of connected users
* Message observer mechanism
* Chat activity logging
* Client version without GUI
* UML architecture diagram

## Technologies

* **Java**
* **Java RMI**
* **Client-Server Architecture**
* **GUI**
* **Observer Pattern**
* **PlantUML**

## Project Structure

```text
Java-RMI-Chat/
│
├── ClientPackage/
│   ├── Klient.java
│   ├── KlientGUIMulti.java
│   └── MessageObserver.java
│
├── ServerPackage/
│   ├── Server.java
│   ├── ServerGui.java
│   ├── FunkcjeImp.java
│   ├── IFunkcje.java
│   ├── IMessageObserver.java
│   └── LogWriter.java
│
├── Main.java
├── diagram.puml
├── README.md
└── .gitignore
```

## Architecture

The application is divided into two main components: **client** and **server**.

The server is responsible for managing connected clients and providing remote functionality through Java RMI. Clients use the remote interfaces to communicate with the server and exchange messages.

### Client

The client package contains:

* **Klient** — client implementation without a graphical interface
* **KlientGUIMulti** — GUI client containing the main client functionality
* **MessageObserver** — handles message-related notifications

### Server

The server package contains:

* **Server** — main server component
* **ServerGui** — graphical interface for the server
* **FunkcjeImp** — implementation of the server-side functionality
* **IFunkcje** — remote interface containing methods available to clients
* **IMessageObserver** — interface used for message and user-related notifications
* **LogWriter** — responsible for writing chat logs

## Communication

The application uses **Java RMI** to enable communication between clients and the server.

The general architecture can be represented as:

```text
                    ┌─────────────────┐
                    │   Server GUI    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     Server      │
                    │                 │
                    │   FunkcjeImp    │
                    └────────┬────────┘
                             │
                         Java RMI
                             │
              ┌──────────────┼──────────────┐
              │              │              │
              ▼              ▼              ▼
          ┌────────┐     ┌────────┐     ┌────────┐
          │Client 1│     │Client 2│     │Client 3│
          └────────┘     └────────┘     └────────┘
              │              │              │
              ▼              ▼              ▼
            User           User           User
```

Clients communicate with the server using methods defined in the `IFunkcje` remote interface.

The observer mechanism is used to notify clients about incoming messages and relevant changes, such as the list of connected users.

## Logging

The application includes the `LogWriter` component, which is responsible for saving chat activity to log files.

This provides a record of communication taking place within the application.

## UML Diagram

The project includes a UML diagram created using **PlantUML**.

The source file is available in:

```text
diagram.puml
```

## Running the Application

The application consists of a server and one or more clients.

A typical startup sequence is:

1. Start the RMI registry/server.
2. Start the chat server.
3. Start one or more client instances.
4. Connect the clients to the server.
5. Exchange messages between connected clients.

The GUI client can be launched using `Main.java`.

> The exact startup procedure may depend on the Java/IDE configuration used to run the project.

## My Contribution

The project was developed as a team assignment.

My contribution included:

* implementing and extending selected application functionalities,
* working on the graphical user interface,
* implementing chat logging using `LogWriter`,
* testing the application and its functionality,
* preparing project documentation,
* proposing ideas for additional functionality,
* identifying and fixing issues during development and testing.

The project was developed collaboratively, so individual components were not developed exclusively by a single team member.

## Project Purpose

The project was created as part of a university course to gain practical experience with distributed applications and remote communication.

The main goal was to implement a multi-client chat application using Java RMI and understand how clients can communicate with a central server through remotely invoked methods.

## Project Status

**Completed — academic team project.**
