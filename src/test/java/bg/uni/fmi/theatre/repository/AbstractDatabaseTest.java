//package bg.uni.fmi.theatre.repository;
//
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//
//public abstract class AbstractDatabaseTest {
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:theatre_test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH");
//        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
//        registry.add("spring.datasource.username", () -> "sa");
//        registry.add("spring.datasource.password", () -> "");
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
//        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "");
//        registry.add("spring.liquibase.enabled", () -> "false");
//    }
//}
