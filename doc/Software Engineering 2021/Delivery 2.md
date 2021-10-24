# Software Engineering Second Delivery

## Technologies and tools that will be used to implement the system:

The client-side Android app is made in *Android studio* using the **Kotlin** programming language. To facilitate communication between the client-side app and the web server **Retrofit** REST client is used. In order to de-serialize JSON format data that is read from the API **Moshi** JSON library is used. The **Coil** toolset is used for loading images from a URL. 

The web server is programmed using **GoLang** programming language, using the *JetBrains GoLand* IDE. To run it, *Docker* is being used inside of a Linux virtual machine. It is running a REST API, which enables sending and receiving data from the client-side app. It currently communicates with the client-side over HTTP, though the eventual plan is to use HTTPS.

For the media server, currently *Apache* web server is being used. The eventual plan is to integrate media hosting functionality into the custom GoLang web server.

For the database part, **PostgreSQL** relational database management system is used. It is running inside of a Linux virtual machine.
## UML Diagrams that explain the system internals:

- [UML deployment diagram](./UML_diagrams/UML_deployment.pdf) - overview of the whole system showing the deployment environment and high level interactions between the main components.

- [UML sequence diagram](./UML_diagrams/UML_sequence.pdf) - describing the interactions between the mobile client-side Android application, GoWebServer and the PostgreSQL database.

- [UML activity diagram](./UML_diagrams/UML_activity.pdf) - shows actions that can a player can perform in the “Mind Game” Android mobile client-side app.

- [UML class diagram](./UML_diagrams/UML_class.pdf) - describing the class specifications and their relationships in the mobile client-side MindGame application. The user interaction wireframe is shown as a supplement to the class diagram. 

