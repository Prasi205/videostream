package com.tm.videostream.pojo;

import javax.validation.constraints.NotBlank;

public class RoleRequestPOJO {

	private int roleId;
	
	@NotBlank(message = "Role cannot be blank")
	private String roleName;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
