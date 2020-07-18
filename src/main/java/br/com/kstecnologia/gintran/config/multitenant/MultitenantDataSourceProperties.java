package br.com.kstecnologia.gintran.config.multitenant;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
@Data
@EqualsAndHashCode(callSuper = false)
public class MultitenantDataSourceProperties extends DataSourceProperties {

	private String tenantId;
	
}
