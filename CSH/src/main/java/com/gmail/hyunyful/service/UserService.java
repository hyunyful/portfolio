package com.gmail.hyunyful.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UserService {
	
	//id 중복검사
	public boolean emailcheck(String email);
	
	//닉네임 중복검사
	public boolean nicknamecheck(String nickname);
	
	//회원가입
	//사진 업로드를 위해 MultipartHttpServletRequest로
	public boolean join(MultipartHttpServletRequest request);
	
	//일반 회원 로그인 처리
	public boolean login(HttpServletRequest request);
	
	//카카오 로그인 토큰 받기
	public String getToken(String code);
	
	//카카오톡 로그인시 사용자 정보 받아오기
	public boolean getKakaoUserInfo(String token, HttpSession session);
	
	//네이버 로그인 전에 상태 토큰 받기
	public String getStateToken();
	
	//네이버 접근토큰 받기
	public String getAccessToken(String state, String code);
	
	//네이버 사용자 정보 받기
	public boolean getNaverUserInfo(String accessToken, HttpSession session);
	
	//비밀번호 확인 절차
	public String pwcheck(HttpServletRequest request);
	
	//비밀번호 재설정
	public boolean resetPw(HttpServletRequest request);
}
