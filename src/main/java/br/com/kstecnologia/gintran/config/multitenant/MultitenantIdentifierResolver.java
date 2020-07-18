package br.com.kstecnologia.gintran.config.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class MultitenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	private static String DEFAULT_TENANT_ID = "00000000000000";

	@Override
	public String resolveCurrentTenantIdentifier() {
		String currentTenantId = MultitenantContext.getTenantId();
		return (currentTenantId != null) ? currentTenantId : DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
