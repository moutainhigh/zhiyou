package com.gc.model;

import com.zy.common.util.Identities;

public class PrincipalBuilder {

	public static Principal build(Long userId, String tgt) {
		Principal principal = new Principal();
		principal.setUserId(userId);
		principal.setTgt(tgt);
		principal.setCsrfToken(Identities.uuid());
		return principal;
	}
}
