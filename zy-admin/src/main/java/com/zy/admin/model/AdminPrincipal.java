package com.zy.admin.model;

import java.io.Serializable;

public class AdminPrincipal implements Serializable {

	private static final long serialVersionUID = -1879025693865919840L;

	private final Long id; // admin id
	private final Long userId; // user id
	private final String username; // username
	private final Long siteId; // site id

	public AdminPrincipal(Long id, Long userId, String username, Long siteId) {
		super();
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.siteId = siteId;
	}

	public Long getId() {
		return id;
	}

	public Long getSiteId() {
		return siteId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

}