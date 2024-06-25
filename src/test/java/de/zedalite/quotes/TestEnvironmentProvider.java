package de.zedalite.quotes;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class TestEnvironmentProvider {

  static PostgreSQLContainer<?> DB_CONTAINER;

  static {
    // No try-with-resources is intentionally used here
    // "The singleton container is started only once when the base class is loaded. The container can then be used by all inheriting test classes.
    // At the end of the test suite the Ryuk container that is started by Testcontainers core will take care of stopping the singleton container."
    // See: https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/
    DB_CONTAINER = new PostgreSQLContainer<>("postgres:latest").withInitScript("database.sql");
    DB_CONTAINER.start();
  }

  @MockBean
  FirebaseApp firebaseApp;

  @MockBean
  FirebaseMessaging firebaseMessaging;

  // TODO initially fill some table to make fixture user/quote generation easier

  @DynamicPropertySource
  static void registerMySQLProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
    registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
  }
}
