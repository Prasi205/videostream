package com.tm.videostream.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoleRequestPOJO {

	private int roleId;

	@NotBlank(message = "Role cannot be blank")
	@Size(min = 4, max = 15, message = "Role Name must be 4 to 15 character")
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
