## Overview
The app is written completely in Java. It utilises the Spring Boot framework, and is managed using Maven.
<p>It provides a back-end API interface for an ATM.</p>
<p>The app can be interfaced with using HTTP Requests (using Postman, curl, browser, etc.)</p>
<p>A Dockerfile and docker-compose.yml file are also provided for easy installation.</p>

## Installation
### Without Docker
<p>To install the app, clone into a local repository.</p>
Then, go to the root directory and run `mvn clean package`
<p>Finally, run `mvn spring-boot:run` to start the app.</p>
<p>The app can now be accessed on port 8085.</p>

### With Docker
<p>To install the app, clone into a local repository.</p>
<p>Then, go to the root directory and run `mvn clean package`</p>
<p>Now, run `docker-compose build`</p>
<p>Finally, run `docker-compose up`</p>
<p>The app can now be accessed on port 8085.</p>

## User Manual
<p>To use the app, load it using the above instructions, and then navigate to `localhost:8085/atm/`</p>
<p>This initialises the accounts and ATM balance set out in requirements.</p>
<p>After this, you can navigate to a number of API calls:</p>

* `localhost:8085/atm/balance/{account-number}/{pin}` \
Returns balance for account
* `localhost:8085/atm/maxWithdrawal/{account-number}/{pin}` \
Returns the max withdrawal limit for the account
* `localhost:8085/atm/withdraw/{account-number}/{pin}/{withdrawal-amount}` \
Attempts to withdraw money for the account

<p>Note: if you enter the wrong pin three times, you will lock the account.</p>
<p>For development purposes, the can be overridden by accessing a back-door by entering **** instead of the pin</p>
<p>Now, enter the correct pin and you can access the account. This would be removed for production.</p>

<p>Account 1: Account Number 123456789 ||| Pin 1234</p>
<p>Account 2: Account Number 987654321 ||| Pin 4321</p>

## Comments/Observations
<p>I would have liked to implement the cash dispensing better. A useful design pattern for this is know as 'Chain of Responsibility':</p>
<p>https://www.geeksforgeeks.org/chain-responsibility-design-pattern/</p>
<p>However, this ATM system poses a particular problem due to the low numbers of notes.</p>
<p>For example, if there is only one €50 note and four €20 notes, the pattern would load the 50, then the 20.</p>
<p>Without any other notes, it could not complete the transaction. This is because the tradition CoR pattern doesn't look ahead.</p>
<p>A solution to this is available online, but I opted for my own sub-optimal, yet unique way of implementing the dispensing to be some way unique.</p>
<p>I would have liked to have built a front end and database (easiest to opt for MySQL but potentially more suitable to go with PostgreSQL for this business case since it is ACID compliant).</p>
<p>Further extension of the system would incorporate these features - running three docker containers ((1)back-end/logic/API, (2)front-end and (3)database).</p>
<p>I would also use auth tokens for user sessions to improve scalability and security.</p>

