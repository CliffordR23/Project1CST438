# Project 01 Phone Lookup

[Video Walkthrough] [here](https://youtu.be/8AHUtpmMn_A)

[Github Repo] [here](https://github.com/CliffordR23/Project1CST438)

## Overview
This is a phone lookup app that makes use of an API we found [here](https://github.com/public-apis/public-apis?tab=readme-ov-file).

We got styling help for this document from [this guide](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)

## Introduction

* How was communication managed
  * A slack channel where updates and PR notifications were commumcated across the whole group to promote effective workflows and product development. 
* How many stories/issues were initially considered
   * 14
* How many stories/issues were completed
  * 3
## Team Retrospective

### Ruben Rivera
1.  Pull requests are [here](https://github.com/CliffordR23/Project1CST438/issues?q=is%3Apr%20author%3A%40me)
2.  Github issues are [here](https://github.com/CliffordR23/Project1CST438/issues?q=is%3Aissue%20author%3A%40me)

#### What was your role / which stories did you work on
I worked on the whole login/sign up pages both front end and back end. I also implemented Oauth, local auth, and junit tests.

+ What was the biggest challenge? 
  + Implementing Oauth took a long time, but the biggest challenge was when I messed up the repo.
+ Why was it a challenge?
  + It messed up everyones dev so we had roll back the commits to a working dev.
+ How was the challenge addressed?
  + We all communicated and deciding rolling back and losing just my changes was the way to go, and I had the code saved so it all worked out.
+ Favorite / most interesting part of this project
  + Making the UI I enjoyed a lot.
+ If you could do it over, what would you change?
  + Start testing sooner, i waited too long to start testing my features and ended up having to debug a lot of it.
+ What is the most valuable thing you learned?
  + How to work as a team and communicate more efficently.

### Clifford Rodriguez
1.  pull requests are [here](https://github.com/CliffordR23/Project1CST438/pulls?q=is%3Apr+author%3ACliffordR23+is%3Aclosed)
2.  Github issues are [here](https://github.com/CliffordR23/Project1CST438/issues?q=is%3Aissue%20state%3Aopen%20author%3ACliffordR23)

#### What was your role / which stories did you work on
I mainly worked on creating the verificatino page and routing of API to recive phone number information provided upon look up

+ What was the biggest challenge? 
  + Managing PR's and merges as well as momentous development
+ Why was it a challenge?
  + there was not alot of familiarity with Github across the group and I strugged with creating momentum in my code and efficient development
  + How was the challenge addressed?
  + I communicated with my group and tried to submit work that would allow contributions across said group.
+ Favorite / most interesting part of this project
  + Getting the api to work and seeing my phone number informatino in App form
+ If you could do it over, what would you change?
  + Be more proactive and do this read me FIRST
+ What is the most valuable thing you learned?
  + How to navigate github issues and resolve conflicts

### Angel Valdez

- a link to your [pull requests](https://github.com/CliffordR23/Project1CST438/pulls?q=is%3Apr+author%3AAngel-Valdez+is%3Aclosed)
- a link to your [issues](https://github.com/CliffordR23/Project1CST438/issues?q=is%3Aissue%20state%3Aopen%20assignee%3AAngel-Valdez%20author%3AAngel-Valdez)

#### What was your role / which stories did you work on
I designed and implemented the database. I worked on the App Database, Repository & View Model. I created the User, History & Saved DAOs. I also worked on the Saved Phone Numbers page.

+ What was the biggest challenge? 
  + Getting the database to not crash and Composable UI.
+ Why was it a challenge?
  + There were classes with the same name conflicting with each other, and I had to hard code Kotlin compatibility. I went to office hours to get help from the T.A.
+ Favorite / most interesting part of this project?
  + Wrapping my head around Kotlin and Room Database was a challenge, but a satisfying one to conquer.
+ If you could do it over, what would you change?
  + To start with composable U.I. and to hard code dummy data into the database.
+ What is the most valuable thing you learned?
  + How GitHub and pull requests work. I lost most of my fear for merging code.

### DATABASE DOCUMENTATION

User:
- userID [primary key]
- email
- password

History:
- historyID [primary key]
- userID [foreign key]
- phoneNumber
- date
- time

Saved:
- phoneNumber [primary key]
- userID [foreign key]

User DAO:
- INSERT, DELETE & UPDATE using user class
- SELECT returns all info from user

History DAO:
- INSERT, DELETE & UPDATE using history class
- SELECT returns all history using a userID

Saved DAO:
- INSERT, DELETE & UPDATE using saved class
- SELECT returns all saved using a userID

AppDatabase:
- Creates database if one hasn't been created already.

AppRepository:
- Abstracts the C.R.U.D. functions for each table of the database.

AppViewModel:
- Communicates the C.R.U.D. functions with the U.I.

### Dima Krayilo
1. Dima's pull requests are [here](https://github.com/CliffordR23/Project1CST438/pulls?q=is%3Apr+is%3Aclosed+author%3Abmo-crayola)
1. Dima's Github issues are [here](https://github.com/CliffordR23/Project1CST438/issues?q=is%3Aissue%20state%3Aclosed%20author%3Abmo-crayola)

#### What was your role / which stories did you work on
I worked on the landing page, the history page, trying to merge as much xml to compose as possible, and fixing the navigation

+ What was the biggest challenge?
  + Moving our xml inflated views to compose
+ Why was it a challenge?
  + We didn't read the directions closely enough, and made basically the entire project using xml, and I wasn't able to fix our oauth pages to use compose
  + How was the challenge addressed?
  + We found out about this issue on Tuesday, I spoke to Dr. C. who recommended to use AI to move all the ui bits to compose, but I failed to use it well enough to make oauth and the api work
+ Favorite / most interesting part of this project
  + I enjoyed figuring out conceptually how composeable ui works, and I liked working on a page that pulls data from a database rather than being responsible for the database
+ If you could do it over, what would you change?
  + I would make sure our group has mock data in the DB as early as possible to let me have testable data
+ What is the most valuable thing you learned?
  + Using an AuthManager rather than passing around a userId between activities made keeping track of the user significantly cleaner, and this is something I will use in the future

## Conclusion

- How successful was the project?
  - The project was extremley successful we grew experience in Github UI and development, communication and remidiation strategies, group programming development. API, Database, Auth development.
- What was the largest victory?
  - Overcoming Dev branch issues
- Final assessment of the project
  - Overall we learned how to overcome challennges, learn Git and Github, and Android studio.
