package ru.netology.springbootdemo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDemoApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    private static GenericContainer<?> devApp = new GenericContainer<>("mydevapp:latest")
            .withExposedPorts(8080);
    @Container
    private static GenericContainer<?> prdApp = new GenericContainer<>("myprdapp")
            .withExposedPorts(8081);

    /*
    @BeforeAll
    public static void setUp() {
        devApp.start();
        prdApp.start();
    }
    */

    @Test
    void contextLoads() {
        Integer devPort = devApp.getMappedPort(8080);
        Integer prdPort = prdApp.getMappedPort(8081);
        String urlTmp = "http://localhost:%d/profile";
        String devResponse = restTemplate.getForEntity(String.format(urlTmp, devPort), String.class).getBody();
        //System.out.printf("DevApp: %s\n", devResponse);
        Assertions.assertEquals("Current profile is dev", devResponse);
        String prdResponse = restTemplate.getForEntity(String.format(urlTmp, prdPort), String.class).getBody();
        //System.out.printf("PrdApp: %s\n", prdResponse);
        Assertions.assertEquals("Current profile is production", prdResponse);
    }

}
