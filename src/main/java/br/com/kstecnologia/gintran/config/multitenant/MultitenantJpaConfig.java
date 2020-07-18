package br.com.kstecnologia.gintran.config.multitenant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties({ MultitenantProperties.class, JpaProperties.class })
@EnableTransactionManagement
public class MultitenantJpaConfig {

	@Autowired
	private MultitenantProperties multitenantProperties;

	@Autowired
	private JpaProperties jpaProperties;

	@Bean(name = "dataSources")
	public Map<String, DataSource> dataSources() {

		Map<String, DataSource> result = new HashMap<>();
		for (MultitenantDataSourceProperties dsProperties : this.multitenantProperties.getDataSources()) {

			HikariDataSource dataSource = new HikariDataSource();

			dataSource.setDataSourceJNDI(dsProperties.getTenantId());
			dataSource.setJdbcUrl(dsProperties.getUrl());
			dataSource.setUsername(dsProperties.getUsername());
			dataSource.setPassword(dsProperties.getPassword());
			dataSource.setDriverClassName(dsProperties.getDriverClassName());
			dataSource.setConnectionTimeout(20000);
			dataSource.setIdleTimeout(300000);
			dataSource.setMinimumIdle(5);
			dataSource.setMaximumPoolSize(20);
			dataSource.setMaxLifetime(1200000);
			dataSource.setAutoCommit(true);
			dataSource.setConnectionTestQuery("SELECT 1");

			result.put(dsProperties.getTenantId(), dataSource);

		}

		return result;

	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		return new DataSourceMultitenantConnectionProvider();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new MultitenantIdentifierResolver();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
			MultiTenantConnectionProvider multiTenantConnectionProvider,
			CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {

		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

		// No dataSource is set to resulting entityManagerFactoryBean
		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setPackagesToScan(new String[] { "br.com.kstecnologia.gintranws" });
		result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		result.setJpaPropertyMap(hibernateProps);

		return result;

	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		return transactionManager;

	}

}
