# Training Notes for Java Security

> Notes start at the first OWASP Top 10 exercise: SQL Injection

- The solution for the **SQL Injection** is:

```java
public List<AccountDTO> findAccountsByCustomerIdPS(String customerId) throws SQLException {
    String jql = "select * from Account where customer_id=?";
    final PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(jql);
    preparedStatement.setString(1, customerId);
    ResultSet rs = preparedStatement.executeQuery();

    List<AccountDTO> accounts = new ArrayList<>();
    while (rs.next()) {
        AccountDTO acc = AccountDTO.builder()
                .id(rs.getLong("id"))
                .customerId(rs.getString("customer_id"))
                .name(rs.getString("name"))
                .balance(rs.getLong("balance"))
                .build();

        accounts.add(acc);
    }

    return accounts;
}
```

- In **Broken Access Control** mention these:
    - ACL, ABAC, RBAC (and also that we'll talk about these later) 

- Things to talk about the **Misconfiguration** solution:
    - Use the proper `application.properties`
    - Check for potential hacks in the file upload (`..` for example)
    - Check file type
    - Check mime type
    - Use transformers that would fail if uploaded file was not the correct type

- In **XSS**, use this input first: `<b onmouseover=alert('Wufff!')>click me!</b>`
    - Then use an image: `<img src="https://images.dog.ceo/breeds/retriever-golden/n02099601_2663.jpg" />`
    - Then steal cookies:
        ```javascript
        <script>console.log("Document:" + document.cookie);</script>
        ```
- In **Deserialization** create a `UserDTO` that only holds the acceptable fields and map it to a `User` object.
- In **Known Vulnerabilities**
    - run `./mvnw clean install -Powasp-dependency-check`
    - Show [NVD](https://nvd.nist.gov/) (National Vulnerability Database)
    - Search for Spring
    - Mention Kyro that's on the list (with regards to deserialization)
    - Addd spring cloud config:
      ```xml
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-config</artifactId>
      </dependency>
      ```
- In **CORS** 
    - Start the `cors-server` and `cors-client` projects
    - try loading `localhost:9090/simple-cors`
    - You will get an error
    - Solution is: `@CrossOrigin(origins = "http://localhost:9090")`
- In **Clickjacking**
- In **Form Tampering**
    - Use: 
    ```
    <button 
        type="submit" 
        form="profile-form"
        formaction="http://localhost:8080/steal-your-data"
        style="position:relative;top:94px;left:28px;"
        type="submit"
    >Update</button>
    ```
- In `ReadJavaHome` the solution is:
  ```java
  File policy = new File("src/main/resources/home.policy");
  System.setProperty("java.security.policy", policy.getAbsolutePath());
  System.setSecurityManager(new SecurityManager());
  System.out.printf("java.home is : %s%n", System.getProperty("java.home"));
  ```

## Spring Security

### Adding Spring Security

Add these to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

Create `WebSecurityConfig` in the `config` package:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
```

Talk about how `configure` works and how `UserDetailsService` works.

Then craete the `login` page.

```html
<!DOCTYPE html>
<html
        xmlns="http://www.w3.org/1999/xhtml"
        xmlns:th="https://www.thymeleaf.org"
        xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
>
<head>
    <title>Spring Security Example </title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body>
<div class="container mt-2">
    <div th:if="${param.error}" class="alert alert-danger">
        Invalid username and password.
    </div>
    <div th:if="${param.logout}" class="alert alert-success">
        You have been logged out.
    </div>
    <form th:action="@{/login}" method="post" class="card">
        <h5 class="card-header">
           Log In
        </h5>
        <div class="card-body">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" name="username" id="username" class="form-control"/>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" name="password" id="password" class="form-control"/>
            </div>
            <button class="btn btn-primary" type="submit">Go</button>
        </div>
    </form>
</div>
</body>
</html>
```

and modify the `hello.html` to include a logout form:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <title>Hello World!</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1 th:inline="text">Hello [[${#httpServletRequest.remoteUser}]]!</h1>
    <form th:action="@{/logout}" method="post">
        <input type="submit" value="Sign Out" class="btn btn-primary"/>
    </form>
</div>
</body>
</html>
```

Try it out! (`http://localhost:8080/home`)

Tell them that this works because spring has a default implementation.

Show them the injected CSRF token and the headers that are added by default.


Now let's look at the password encoder. It is not secure! Reimplement it using our
own implementation (Argon2):

```java
@Bean
public Argon2PasswordEncoder passwordEncoder() {
    return new Argon2PasswordEncoder();
}

// ...

UserDetails user = User.builder()
        .username("user")
        .password("password")
        .roles("USER")
        .passwordEncoder(passwordEncoder()::encode)
        .build();
```

Talk about why this is necessary:

- Plain text passwords
- hashed passwords > rainbow tables
- salted passwords > brute force
- state of the art: slow encoders like argon1
- corollaray: we need tokens so that passwords are only checked once

Show them what `User`, `UserDetails` and `UserDetailsService` is!

Talk about how to traverse the source code (Ctrl + Click and Download Sources).

Now look at the log after running `./mvnw spring-boot:run`.

Talk about all the filters that are present.

## Implementing DaoAuthenticationProvider

Now let's add a new implementation of `DaoAuthenticationProvider`:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {

    private static Logger LOGGER = LoggerFactory.getLogger(MyDaoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("Trying to authenticate with: {}", authentication);
        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.info("Checking if supports with: {}", authentication);
        return super.supports(authentication);
    }
}
```

Use it from the security config:

```java
@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new MyDaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService());
    return provider;
}
```

Restart the application and see the log messages. Talk about how this can be further
expanded into a database-backed authentication method.

Talk about how Spring automatically protects against session fixation by creating an new
`JSESSIONID` whenever it is necessary.

## Thymeleaf Security

Add this as a maven dependency:

```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```

Replace the previous hello message with:

```html
<h1 sec:authorize="isAuthenticated()">
        Hello, <span sec:authentication="name"></span>
</h1>
```

Restart the app, and see the result.

## Debugging Spring Security

Modify the `@EnableWebSecurity` to `@EnableWebSecurity(debug = true)`.

Restart the application and see the result.

Restore the original version.

Add this to `application.yml`:

```yml
logging:
  level:
    org:
      springframework:
        security:
          web:
            FilterChainProxy: DEBUG
```

See the result.

Restore the original.

## Method Security

Add a new user with an additional `ADMIN` role:

```java
@Bean
@Override
public UserDetailsService userDetailsService() {
    UserDetails user = User.builder()
            .username("user")
            .password("password")
            .roles("USER")
            .passwordEncoder(passwordEncoder()::encode)
            .build();
    UserDetails admin = User.builder()
            .username("admin")
            .password("admin")
            .roles("USER", "ADMIN")
            .passwordEncoder(passwordEncoder()::encode)
            .build();
    return new InMemoryUserDetailsManager(user, admin);
}
```

Create a new `Controller` that renders an admin page:

```java
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "admin";
    }
}
```

Enable global method security by creating this new config:

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MethodSecurityConfig
        extends GlobalMethodSecurityConfiguration {
}
```

Implement the admin page:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
      xmlns:th="https://www.thymeleaf.org">
<head>
    <title>Admin</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body>
<div class="container mt-2">
    <div class="card">
        <h5 class="card-header">Hello, <span sec:authentication="name"></span></h5>
        <div class="card-body">
            You're on the admin page!
            Go <a th:href="@{/home}">Home</a>.
        </div>
    </div>
</div>
</body>
</html>
```

See how it works for users and admins. Users get an exception. This is not good.

## Custom Error Handlers

Create an `AccessDeniedHandler`:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException e
    ) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn("User: {} attempted to access the protected URL: {}",
                    auth.getName(), request.getRequestURI());
        }

        response.sendRedirect(request.getContextPath() + "/accessDenied");
    }
}
```

Add it to the security config as a `@Bean`:

```java
@Bean
public AccessDeniedHandler accessDeniedHandler(){
    return new CustomAccessDeniedHandler();
}
```

Add an access denied page to the security config:

```java
.exceptionHandling()
    .accessDeniedPage("/access-denied")
    .and()
```

And add the corresponding view, `access-denied.html`:

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="https://www.thymeleaf.org" xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <title>Spring Security Example</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>You don't have access to this page!</h1>
    <p>Go <a th:href="@{/home}">home</a>.</p>
</div>
</body>
</html>
```

And finally the view registration in `MvcConfig`:

```java
registry.addViewController("/access-denied").setViewName("access-denied");
```

## Authentication Success Handling

Change the `formLogin` in the security config to this:

```java
.formLogin()
    .loginPage("/login")
    .loginProcessingUrl("/login")
    .defaultSuccessUrl("/hello", true)
    .permitAll()
    .and()
```

Show them how this works.

Now create a success manager (and explain it):

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final Map<String, String> targetLookup = new HashMap<>();

    {
        targetLookup.put("ROLE_USER", "/hello");
        targetLookup.put("ROLE_ADMIN", "/admin");
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException {
        handle(request, response, authentication);
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        handle(request, response, authentication);
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()) {
            LOGGER.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (targetLookup.containsKey(authorityName)) {
                return targetLookup.get(authorityName);
            }
        }
        throw new IllegalStateException();
    }
}
```

Add a success manager to the security config:

```java
@Bean
public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
    return new CustomAuthenticationSuccessHandler();
}
```

And add change the `successUrl` to a `successHandler` in the `formLogin` section:

```java
successHandler(customAuthenticationSuccessHandler())
```

Profit.

## Remember Me

Add a token repository:

```java
@Bean
public PersistentTokenRepository persistentTokenRepository() {
    return new InMemoryTokenRepositoryImpl();
}
```

Add remember me config to the security config:

```java
.rememberMe()
    .tokenRepository(persistentTokenRepository())
    .rememberMeParameter("remember-me")
    .and()
```

then add this checkbox to the login form:

```html
<div class="form-check">
    <input type="checkbox" name="remember-me" id="remember-me" class="form-check-input"/>
    <label for="remember-me" class="form-check-label">Remember Me</label>
</div>
```

## OAuth 2

Add the OAuth dependency:

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

Create a new OAuth app on GitHub [here](https://github.com/settings/developers).

Homepage: `http://localhost:8080`
CallbackURL: `http://localhost:8080/login/oauth2/code/github`

Add the Client ID and Secret to `application.yml` (use env vars!!):

```yml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: github-client-id
            clientSecret: github-client-secret
```

Enable OAuth login in the security config:

```java
.oauth2Login()
    .and()
```

Disable form login.

Show how this works...

Then tell them that a complete configuration of OAuth is out of the scope of this training as it
is very complex...undo the changes.

## CORS

Show the simple config:

```java
.cors(withDefaults())
```

Then show the fine-grained version:

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:9090"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

Add `"/api/**"` to the `permitAll` part.

And add the `/api/simple-cors` endpoint:

```java
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CORSController {

    @GetMapping("/api/simple-cors")
    public String simpleCors() {
        return "ok";
    }
}
```

Now open up the `cors-client` program (we used it earlier), start it and navigate to
`http://localhost:9090/simple-cors` and look at the logs. It should work.

Now comment out the cors config `@Bean` and try it again. It won't work.

## Preventing Brute Force

Create a login attempt service:

```java
public class LoginAttemptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);
    private static final AtomicInteger DEFAULT_COUNTER = new AtomicInteger();
    
    private final int MAX_ATTEMPTS = 5;
    private final ConcurrentMap<String, AtomicInteger> attemptsLookup = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsLookup.remove(key);
    }

    public void loginFailed(String key) {
        attemptsLookup.putIfAbsent(key, new AtomicInteger());
        AtomicInteger attempts = attemptsLookup.get(key);
        attempts.incrementAndGet();
        LOGGER.info("Login failed for key: {}. Attempts: {}}", key, attempts.get());
    }

    public boolean isBlocked(String key) {
        return attemptsLookup.getOrDefault(key,DEFAULT_COUNTER).get() >= MAX_ATTEMPTS;
    }
}
```

Add it as a bean to the security config: 

```java
@Bean
public LoginAttemptService loginAttemptService() {
    return new LoginAttemptService();
}
```

Add an authentication failure listener:

```java
@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;

    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                e.getAuthentication().getDetails();
        loginAttemptService.loginFailed(auth.getRemoteAddress());
    }
}
```

Tell them why we need to track the IP, and not the user (they can attempt with any user).

Now modify the success handler to notify the `LoginAttemptService`:

```java
private LoginAttemptService loginAttemptService;

public CustomAuthenticationSuccessHandler(LoginAttemptService loginAttemptService) {
    this.loginAttemptService = loginAttemptService;
}
```

fix the security config:

```java
@Bean
public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
    return new CustomAuthenticationSuccessHandler(loginAttemptService());
}
```

and notify the `LoginAttemptService`:

```java
loginAttemptService.loginSucceeded(request.getRemoteAddr());
```

Create a new exception for the ban event:

```java
public class YouAreABadPersonException extends AuthenticationException {
    public YouAreABadPersonException(String msg) {
        super(msg);
    }
}
```

Modify the `MyDaoAuthenticationProvider` to take attempts into account:

```java
private LoginAttemptService loginAttemptService;

public MyDaoAuthenticationProvider(LoginAttemptService loginAttemptService) {
    this.loginAttemptService = loginAttemptService;
}

@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        if(loginAttemptService.isBlocked(request.getRemoteAddr())) {
            throw new YouAreABadPersonException("And you are banned");
        }
    }
    LOGGER.info("Trying to authenticate with: {}", authentication);
    return super.authenticate(authentication);
}
```

and fix the security config:

```java
DaoAuthenticationProvider provider = new MyDaoAuthenticationProvider(loginAttemptService());
```

Then add a custom error handler for our exception:

```java
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BadPersonHandler implements AuthenticationFailureHandler {

    private final AuthenticationFailureHandler delegate = new SimpleUrlAuthenticationFailureHandler(
            "/login?error");

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        if(exception instanceof YouAreABadPersonException) {
            response.getWriter().write("You are a bad person");
        } else {
            delegate.onAuthenticationFailure(request, response, exception);
        }
    }
}
```

and reconfigure security config with this:

```java
@Bean
public AuthenticationFailureHandler badPersonHandler() {
    return new BadPersonHandler();
}

// ...

.failureHandler(badPersonHandler())

```