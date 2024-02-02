package com.tm.videostream.pojo.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequestPOJO {

	private int userId;

	@NotBlank(message = "Name cannot be blank")
	@Size(min = 5, max = 25, message = "Name must be 6 to 25 character")
	private String name;

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
	private String email;

	@NotBlank(message = "Username cannot be blank")
	@Size(min =5, max = 25, message = "Username must be 5 to 25 character")
	@Pattern(regexp = "^(?=\\S+$)[a-zA-Z]+$", message = "Username not accept numbers and any other special character")
	private String username;

	@NotBlank(message = "Password cannot be blank")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])(?=\\S+$).{8,15}$",
	         message = "Password need one upper,one lower,one number,one special character and 8 to 15 letters")
	private String password;

	@NotBlank(message = "Role cannot be blank")
	@Size(min = 4, max = 15, message = "RoleName must be 4 to 15 character")
	private String roleName;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
