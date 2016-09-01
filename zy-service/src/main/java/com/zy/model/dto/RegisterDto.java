package com.gc.model.dto;

import com.gc.entity.usr.User.UserType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
public class RegisterDto implements Serializable {

	@NotNull
	private UserType userType;

	@NotBlank
	private String password;

	@Pattern(regexp = "^1[\\d]{10}$")
	@NotBlank
	private String phone;
	@NotBlank
	@Length(min = 2, max = 10)
	private String nickname;

	@Pattern(regexp = "^[\\d]{5,11}$")
	@NotBlank
	private String qq;

	private Long inviterId;

	private String registerIp;

}