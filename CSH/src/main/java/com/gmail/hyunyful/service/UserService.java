package com.gmail.hyunyful.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UserService {
	
	//id 중복검사
	public boolean idcheck(String id);
	
	//회원가입
	//사진 업로드를 위해 MultipartHttpServletRequest로
	public boolean join(MultipartHttpServletRequest request);
	
	//일반 회원 로그인 처리
	public boolean login(HttpServletRequest request);
	
	//카카오 로그인 토큰 받기
	public String getToken(String code);
	
	//카카오톡 로그인시 사용자 정보 받아오기
	public Map<String,Object> getKakaoUserInfo(String token);
}
