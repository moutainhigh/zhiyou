package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
public class AgentRegisterDto implements Serializable {

	@NotBlank
	private String openId;

	@NotBlank
	@URL
	private String avatar;

	@NotBlank
	private String nickname;

	@NotBlank
	@Pattern(regexp = "^1[\\d]{10}$")
	private String phone;

	private String registerIp;

	private Long inviterId;

}
