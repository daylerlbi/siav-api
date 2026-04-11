package com.sistemas_mangager_be.edu_virtual_ufps.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import java.util.Map;


@Profile("prod")
@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@EntityScan(basePackages = "com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities")
@EnableJpaRepositories(basePackages = "com.sistemas_mangager_be.edu_virtual_ufps.oracle.repositories", entityManagerFactoryRef = "oracleEntityManagerFactory", transactionManagerRef = "oracleTransactionManager")
public class OracleDataSourceConfig {

    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "oracle.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
            @Qualifier("oracleDataSource") DataSource oracleDataSource) {

        return entityManagerFactoryBuilder
                .dataSource(oracleDataSource)
                .packages("com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities")
                .persistenceUnit("oracle")
                .properties(Map.of("hibernate.dialect", "org.hibernate.dialect.OracleDialect"))
                .build();
    }

    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
