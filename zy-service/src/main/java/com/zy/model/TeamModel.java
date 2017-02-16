package com.zy.model;

import com.zy.entity.usr.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamModel {
	private User user;
	private List<User> v4Children = new ArrayList<>();
	private List<User> directV4Children = new ArrayList<>();
}