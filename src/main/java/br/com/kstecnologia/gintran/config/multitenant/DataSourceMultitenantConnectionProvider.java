package br.com.kstecnologia.gintran.config.multitenant;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class DataSourceMultitenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final long serialVersionUID = 8098234728145424794L;

	@Autowired
	private Map<String, DataSource> dataSources;

	@Override
	protected DataSource selectAnyDataSource() {
		return this.dataSources.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		return this.dataSources.get(tenantIdentifier);
	}

}
