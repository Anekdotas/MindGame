![VU logo](./documentation images/PBL-title-page.png)

# 1. Purpose of the system

## 1.1 Users

_**Anonymous user** -  user that has not logged into the application. Initially all users, apart from the developers, will be anonymous (while the login system is not properly implemented)._

_**_Authorized user_** - user that has logged into the application, this will provide more functionality in the application in comparison to being anonymous._

_**_Developer/Tester_** - user that takes part in the development or testing of the application, has all the privileges._

## 1.2 Location
Initially: Team’s “Anekdotas” GitLab repository
Eventually (upon the main release): The Google Play app store for Android

## 1.3 Responsibilities
	
##### The primary responsibilities of the system:
- Provide the user with interactive quiz levels.
- Be able to effectively engage with the user by providing a different environment than most applications of such kind.
- Provide the user with interesting and engaging content in the form of fun, thought provoking quiz questions.
- Allow access to levels based on user profile achievements.

##### Secondary responsibilities of the system:
- Allow users to get ingame currency by completing levels.
- Allow authorized users to buy “power-ups” and “upgrades” with ingame currency.

## 1.4 Need

This system is needed in order to bring entertainment for the users and to increase diversity among quiz apps.



# 2. High-level overview of the system

The system will be of a client-server type that contains:
- The Android Quiz App - a mobile client-side application with a functional user interface that enables users to play levels. 
- The Web Server - a server, which can facilitate the communication between the mobile application and the database.
- The Database - a relational database which stores quiz levels, user data and other relevant information.

![alternative text](./documentation images/High-level%20overview.jpg)



# 3. Functional Requirements

## 3.1 High Priority
- As a curious user I wish to learn new things in an interesting environment by completing quizzes, so that I would not get bored while playing.
- As a user, I want to get access to different types of question topics, so I can diversify my experience.
- As a user, I want to see my statistics, so I can have fun competing with my friends.
- As a user, I want to create my personal account, so I can use the app on different devices and keep my progress.
- As a developer I would like to easily add new quiz levels without the need of editing the client-side (mobile) application code.

## 3.2 Medium Priority
- As a creative, enthusiastic user, I want to share levels created by me, so that my friends can get a better experience.  
- As a player, I want to experience different game modes (picture guessing, audio levels, etc.) to make my experience more varied, joyful and engaging.
- As a user, I want to log-in into my account, so I can see my progress, ingame currency and continue playing on different devices.

## 3.3 Low Priority
- As a non-native English speaker I would like the application to have the ability to switch to a different language in order to make understanding the interface easier.
- As an enthusiastic and inventive player, I want to conveniently create my own levels and upload them, so my friends can see them and play.


	
# 4. Non-Functional requirements

## 4.1 Compatibility:
- The mobile app must be able to run on Android 6.0 or greater.
- Implement unicode compatibility in every step of the system: users must be able to type, submit and see non-latin characters.
- The system must run in VU infrastructure.

## 4.2 Reliability: 
- The system should be reliable enough to provide an enjoyable, non-frustrating experience to the user.

## 4.3 Security:
- The database and the server should be in separate Virtual Machines due to security reasons.

## 4.4 Performance: 
- Level loading should be optimized in such a way that on a 3G clients’  internet connection, a level shouldn’t take an unreasonably long time to load. 
