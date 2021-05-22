package be.digitcom.course.management.config.persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final Environment env;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_ENGINE}")
    private String dbEngine;

    public HibernateConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setJpaVendorAdapter(getJpaVendorAdapter());
        lcemfb.setDataSource(dataSource());
        lcemfb.setPersistenceUnitName("entityManagerFactory");
        lcemfb.setPackagesToScan("be.digitcom.course.management.model");
        lcemfb.setJpaProperties(hibernateProperties());

        return lcemfb;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public JpaVendorAdapter getJpaVendorAdapter() {
        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();

        return adapter;
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dbEngine + "://" + dbHost + "/" + dbName);
        dataSourceBuilder.username(dbUsername);
        dataSourceBuilder.password(dbPassword);
        dataSourceBuilder.driverClassName("org.postgresql.Driver");

        return dataSourceBuilder.build();
    }

    private final Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect=", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("hibernate_format_sql", env.getProperty("hibernate_format_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.max_fetch_depth", env.getProperty("hibernate.max_fetch_depth"));
        properties.put("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
        properties.put("hibernate.cache.use_minimal_puts", env.getProperty("hibernate.cache.use_minimal_puts"));
        properties.put("hibernate.connection_realease_mode", env.getProperty("hibernate.connection_realease_mode"));
        properties.put("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));

        return properties;
    }
}
