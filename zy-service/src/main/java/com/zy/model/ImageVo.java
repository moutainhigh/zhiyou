package com.zy.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ImageVo implements Serializable {

	private String image;

	private String imageThumbnail;

	private String imageBig;

}
