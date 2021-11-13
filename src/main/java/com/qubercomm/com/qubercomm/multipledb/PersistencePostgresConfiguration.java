package com.qubercomm.multipledb;


import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@PropertySource({ "classpath:application.properties" })
@EnableJpaRepositories(
    basePackages = "com.qubercomm.multipledb.repository.postgres", 
    entityManagerFactoryRef = "userEntityManager", 
    transactionManagerRef = "userTransactionManager"
)
public class PersistencePostgresConfiguration {
    @Autowired
    private Environment env;
    
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager() {
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        em.setPackagesToScan(
          new String[] { "com.qubercomm.multipledb.model.postgres" });

        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
          env.getProperty("primary.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
          env.getProperty("primary.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public DataSource userDataSource() {
 
        DriverManagerDataSource dataSource
          = new DriverManagerDataSource();
        dataSource.setDriverClassName(
          env.getProperty("primary.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("primary.datasource.url"));
        dataSource.setUsername(env.getProperty("primary.datasource.username"));
        dataSource.setPassword(env.getProperty("primary.datasource.password"));

        return dataSource;
    }

    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager() {
 
        JpaTransactionManager transactionManager
          = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
          userEntityManager().getObject());
        return transactionManager;
    }
}