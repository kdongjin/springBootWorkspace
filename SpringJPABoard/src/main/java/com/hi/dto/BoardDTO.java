package com.hi.dto;

import java.util.Date;

import lombok.Data;


@Data
public class BoardDTO {
	private long boardno; 
	private String title; 
	private String content; 
	private String writer; 
	private Date regdate; 
}
