package com.gmail.hyunyful.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UserService {
	
	//id 중복검사
	public boolean idcheck(String id);
	
	//회원가입
	//사진 업로드를 위해 MultipartHttpServletRequest로
	public boolean join(MultipartHttpServletRequest request);
}
