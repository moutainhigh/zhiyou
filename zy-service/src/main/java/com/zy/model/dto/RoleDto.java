package com.gc.model.dto;

import com.gc.entity.adm.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RoleDto extends Role {

	private Map<String, String> permissions = new HashMap<>();

}
