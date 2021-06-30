# Railway-Reservation-System  ``` Backend Project```
This Project is to make ticketing more convenient for travelers, Indian Railways has started an online reservation system, which helps us in booking tickets from the comfort of our homes or offices. ... You will then use this account to book a railway ticket and also cancel a railway reservation that you have made.

## Technologies

### Build Tool
* Maven

### Framework
* Spring

### DataBase
* MongoDB

### Messaging
* Spring Email
* RabbitMQ

### Testing
* Junit Testing
* Mockito

### Api Tool
* Postman

### IDE
* Intellij

### Deploy
* AWS Lambda
* AWS S3

## Maven

Building a software project typically consists of such tasks as downloading dependencies, putting additional jars on a classpath, compiling source code into binary code, running tests, packaging compiled code into deployable artifacts such as JAR, WAR, and ZIP files, and deploying these artifacts to an application server or repository.

Apache Maven automates these tasks, minimizing the risk of humans making errors while building the software manually and separating the work of compiling and packaging our code from that of code construction.

In this tutorial, we're going to explore this powerful tool for describing, building, and managing Java software projects using a central piece of information — the Project Object Model (POM) — that is written in XML.

### Project Object Model
The configuration of a Maven project is done via a Project Object Model (POM), represented by a pom.xml file. The POM describes the project, manages dependencies, and configures plugins for building the software.

The POM also defines the relationships among modules of multi-module projects. Let's look at the basic structure of a typical POM file:

```javascript
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.1</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.aws</groupId>
    <artifactId>aws-dynamoDB</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>aws-dynamoDB</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>11</java.version>
        <spring-cloud.version>Hoxton.BUILD-SNAPSHOT</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.amazonaws</groupId>
            <artifactId>aws-java-sdk-dynamodb</artifactId>
            <version>1.12.4</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.5.0</version>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
### Dependencies
These external libraries that a project uses are called dependencies. The dependency management feature in Maven ensures automatic download of those libraries from a central repository, so you don't have to store them locally.

This is a key feature of Maven and provides the following benefits:

uses less storage by significantly reducing the number of downloads off remote repositories
makes checking out a project quicker
provides an effective platform for exchanging binary artifacts within your organization and beyond without the need for building artifact from source every time
In order to declare a dependency on an external library, you need to provide the groupId, artifactId, and the version of the library. Let's take a look at an example:

```javascript
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>4.3.5.RELEASE</version>
</dependency>
```
As Maven processes the dependencies, it will download Spring Core library into your local Maven repository.

### Repositories
A repository in Maven is used to hold build artifacts and dependencies of varying types. The default local repository is located in the .m2/repository folder under the home directory of the user.

If an artifact or a plug-in is available in the local repository, Maven uses it. Otherwise, it is downloaded from a central repository and stored in the local repository. The default central repository is Maven Central.

Some libraries, such as JBoss server, are not available at the central repository but are available at an alternate repository. For those libraries, you need to provide the URL to the alternate repository inside pom.xml file:
```javascript
<repositories>
    <repository>
        <id>JBoss repository</id>
        <url>http://repository.jboss.org/nexus/content/groups/public/</url>
    </repository>
</repositories>
```
Please note that you can use multiple repositories in your projects.

### Properties
Custom properties can help to make your pom.xml file easier to read and maintain. In the classic use case, you would use custom properties to define versions for your project's dependencies.

Maven properties are value-placeholders and are accessible anywhere within a pom.xml by using the notation ${name}, where name is the property.

Let's see an example:
```javascript
<properties>
    <spring.version>4.3.5.RELEASE</spring.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
</dependencies>
```
Now if you want to upgrade Spring to a newer version, you only have to change the value inside the<spring.version> property tag and all the dependencies using that property in their <version> tags will be updated.

Properties are also often used to define build path variables:
```javascript
<properties>
    <project.build.folder>${project.build.directory}/tmp/</project.build.folder>
</properties>

<plugin>
    //...
    <outputDirectory>${project.resources.build.folder}</outputDirectory>
    //...
</plugin>
```
### Build
The build section is also a very important section of the Maven POM. It provides information about the default Maven goal, the directory for the compiled project, and the final name of the application. The default build section looks like this:

```javascript
<build>
    <defaultGoal>install</defaultGoal>
    <directory>${basedir}/target</directory>
    <finalName>${artifactId}-${version}</finalName>
    <filters>
      <filter>filters/filter1.properties</filter>
    </filters>
    //...
</build>
```
The default output folder for compiled artifacts is named target, and the final name of the packaged artifact consists of the artifactId and version, but you can change it at any time.

### Using Profiles
Another important feature of Maven is its support for profiles. A profile is basically a set of configuration values. By using profiles, you can customize the build for different environments such as Production/Test/Development:

```javascript
<profiles>
    <profile>
        <id>production</id>
        <build>
            <plugins>
                <plugin>
                //...
                </plugin>
            </plugins>
        </build>
    </profile>
    <profile>
        <id>development</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <build>
            <plugins>
                <plugin>
                //...
                </plugin>
            </plugins>
        </build>
     </profile>
 </profiles>
 ```
As you can see in the example above, the default profile is set to development. If you want to run the production profile, you can use the following Maven command:

```javascript
mvn clean install -Pproduction
```
## Spring Framework

The Spring Framework is a mature, powerful and highly flexible framework focused on building web applications in Java.

One of the core benefits of Spring is that it takes care of most of the low-level aspects of building the application to allow us to actually focus on features and business logic.

Another strong point is that, while the framework is quite mature and well-established, it's very actively maintained and has a thriving dev community. This makes it quite up to date and aligned with the Java ecosystem right now.

Of course, there's a lot to learn to work well with Spring.

### Spring Boot
Spring Boot is an opinionated addition to the Spring platform, focused on convention over configuration — highly useful for getting started with minimum effort and creating standalone, production-grade applications.

This tutorial is a starting point for Boot, in other words a way to get started in a simple manner with a basic web application.

We'll go over some core configuration, a front-end, quick data manipulation, and exception handling.

#### Setup
First, let's use Spring Initializr to generate the base for our project.

The generated project relies on the Boot parent:

```javascript
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.4.0</version>
    <relativePath />
</parent>
```
The initial dependencies are going to be quite simple:
```javascript
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
#### Application Configuration
Next, we'll configure a simple main class for our application:

```javascript
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
Notice how we're using @SpringBootApplication as our primary application configuration class. Behind the scenes, that's equivalent to @Configuration, @EnableAutoConfiguration and @ComponentScan together.

Finally, we'll define a simple application.properties file, which for now only has one property:

```javascript
server.port=8081
```

 #### Spring Security
  let's add security to our application by first including the security starter:
```javascript
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-security</artifactId> 
</dependency>
 ```
By now, we can notice a pattern: Most Spring libraries are easily imported into our project with the use of simple Boot starters.

Once the spring-boot-starter-security dependency is on the classpath of the application, all endpoints are secured by default, using either httpBasic or formLogin based on Spring Security's content negotiation strategy.

That's why, if we have the starter on the classpath, we should usually define our own custom Security configuration by extending the WebSecurityConfigurerAdapter class:
```javascript
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .permitAll()
            .and().csrf().disable();
    }
}
 ```
In our example, we're allowing unrestricted access to all endpoints.

#### Web and the Controller
Next, let's have a look at a web tier. And we'll start by setting up a simple controller, the BookController.

We'll implement basic CRUD operations exposing Book resources with some simple validation:

 ```javascript
@RestController("userController")
@RequestMapping("/user")
@Slf4j
public class UserController {

    // *---------------------------------- User Functionalities -------------------------------*


    // *--------- Autowiring Reference Variables --------*
    @Autowired
    private UserService userService;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;
    private String token = "";
    // *---------------------------------------------------------------*


    // *--------------- Login Interface -------------------*
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("Authentication is in Process");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.info("Authentication is failed");
            return ResponseEntity.of(Optional.of("Wrong Username and Password , Please Type it correctly"));
        }
        log.info("Authentication is done");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        token = jwt;
        log.info("token is generated");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    // *---------------------------------------------------*


    // *--------------  Basic Functionality ---------------*
    @GetMapping(path = "/public/welcome")
    @ApiIgnore
    public String user() {
        return "Welcome to User Interface";
    }

    @PostMapping(path = "/nonPublic/addUser")
    public String addUser(@RequestBody User user)    {
        return userService.addUser(user);
    }

    @PostMapping(path = "/nonPublic/addAllUser")
    public String addAllUser(@RequestBody List<User> users) {
        return userService.addAllUser(users);
    }

    @PostMapping(path = "/public/createUser")
    public String createUser(@RequestBody UserForm1 userForm1) {
        return userService.createUser(userForm1);
    }

    @GetMapping(path = "/public/getAllUsers")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "/public/userExistById/{user_id}")
    public boolean userExistById(@PathVariable Long user_id) {
        return userService.userExistById(user_id);
    }

    @PostMapping(path = "/public/updateUser")
    public String updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping(path = "/public/userCredential/{user_id}")
    public String getCredentials(@PathVariable long user_id){return userService.getCredentials(user_id);}

    private Long validateUser() {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(payload);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(payload);
        }catch (JSONException err){
            log.info(err.getMessage());
            return -1L;
        }
        String username = jsonObject.getString("sub");
        return credentialsRepository.findAll().stream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0).getUser_id();
    }

    @GetMapping(path = "/public/getUser/{user_id}")
    public User getUser(@PathVariable long user_id){return userService.getUser(user_id);}

    @PostMapping("/public/changePassword")
    public String changePassword(@RequestBody ChangePassword changePassword){return userService.changePassword(changePassword);}

    @PostMapping(path = "/public/saveTicket/{account_no}:{pnr}")
    @ApiIgnore
    public String saveTicket(@PathVariable long account_no,@PathVariable long pnr , @RequestBody Ticket ticket){return userService.saveTicket(account_no,pnr,ticket);}
    // *----------------------------------------------------*


    // *------------------------------------------ End of User Functionalities -----------------------------------------*


}
 ```
Given this aspect of the application is an API, we made use of the @RestController annotation here — which is equivalent to a @Controller along with @ResponseBody — so that each method marshals the returned resource right to the HTTP response.

Note that we're exposing our Book entity as our external resource here. That's fine for this simple application, but in a real-world application, we'll probably want to separate these two concepts.

#### Error Handling
Now that the core application is ready to go, let's focus on a simple centralized error handling mechanism using @ControllerAdvice:

 ```javascript
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BookNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Book not found", 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ BookIdMismatchException.class, 
      ConstraintViolationException.class, 
      DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(
      Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
 ```
Beyond the standard exceptions we're handling here, we're also using a custom exception, BookNotFoundException:

 ```javascript
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    // ...
}
 ```
This gives us an idea of what's possible with this global exception handling mechanism. To see a full implementation, have a look at the in-depth tutorial.

Note that Spring Boot also provides an /error mapping by default. We can customize its view by creating a simple error.html:
```javascript
<html lang="en">
<head><title>Error Occurred</title></head>
<body>
    <h1>Error Occurred!</h1>    
    <b>[<span th:text="${status}">status</span>]
        <span th:text="${error}">error</span>
    </b>
    <p th:text="${message}">message</p>
</body>
</html>
 ```
Like most other aspects in Boot, we can control that with a simple property:
```javascript
server.error.path=/error2
 ```
#### Testing
Finally, let's test our new Books API.

We can make use of @SpringBootTest to load the application context and verify that there are no errors when running the app:
```javascript
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextTest {

    @Test
    public void contextLoads() {
    }
}
 ```
Next, let's add a JUnit test that verifies the calls to the API we've written, using RestAssured:

 ```javascript
public class SpringBootBootstrapLiveTest {

    private static final String API_ROOT
      = "http://localhost:8081/api/books";

    private Book createRandomBook() {
        Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private String createBookAsUri(Book book) {
        Response response = RestAssured.given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(book)
          .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }
}
 ```


## Built With

* [Git](https://git-scm.com/downloads) - Git is software for tracking changes in any set of files.  
* [MongoDB](https://www.mongodb.com/) - MongoDB Atlas comes with built-in operational best practices so you can focus on delivering business value and accelerating application development instead of managing databases
* [Intellij](https://www.jetbrains.com/idea/download/) - IntelliJ IDEA is an integrated development environment written in Java for developing computer software. It is developed by JetBrains, and is available as an Apache 2 Licensed community edition, and in a proprietary commercial edition.
* [JDK](https://www.oracle.com/in/java/technologies/javase-jdk15-downloads.html) - The Java Development Kit is an implementation of either one of the Java Platform, Standard Edition, Java Platform, Enterprise Edition, or Java Platform, Micro Edition platforms released by Oracle Corporation in the form of a binary product aimed at Java developers on Solaris, Linux, macOS or Windows.
* [Maven](https://maven.apache.org/download.cgi) - Maven is a build automation tool used primarily for Java projects. Maven can also be used to build and manage projects written in C#, Ruby, Scala, and other languages. The Maven project is hosted by the Apache Software Foundation, where it was formerly part of the Jakarta Project.
 * [Spring Intializer](https://start.spring.io/) - Spring Initializr Build Status · initializr · Basic language generation for Java, Kotlin and Groovy. · Build system abstraction with implementations for Apache Maven.

