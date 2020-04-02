package com.gmail.hyunyful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmail.hyunyful.domain.User;
import com.gmail.hyunyful.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;

	//회원가입 페이지 이동
	@RequestMapping(value="/user/join",method=RequestMethod.GET)
	public String join() {
		return "user/join";
	}

	//회원가입
	@RequestMapping(value="/user/join",method=RequestMethod.POST)
	public String join(MultipartHttpServletRequest request, RedirectAttributes attr) {
		
		//service 호출
		boolean result = service.join(request);
		attr.addFlashAttribute("joinResult",result);
		
		return "redirect:/";
	}
	
	//일반 로그인 처리
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public String login(HttpServletRequest request, RedirectAttributes attr) {
		//service 호출
		boolean result = service.login(request);
		
		//로그인에 실패하면
		if(result != true) {
			//attr에 실패 메세지를 담아서 보내기
			attr.addFlashAttribute("loginResult",result);
		}
		//System.out.println("result는 "+result);
		return "redirect:/";
	}
	
	//카카오 로그인
	@RequestMapping(value="/user/kakaologin")
	public String getCode(@RequestParam("code") String code, HttpSession session, RedirectAttributes attr) {
		//System.out.println("받은 코드의 값은 "+code);
		
		//서비스의 getToken() 호출
		String access_token = service.getToken(code);
		//System.out.println("얻은 토큰의 값은 "+access_token);
		
		//서비스의 유저정보 불러오는 메소드 호출
		boolean result = service.getKakaoUserInfo(access_token, session);
		attr.addFlashAttribute("msg",result);
		
		//로그인에 성공하면 메인으로
		return "redirect:/";
	}

	//네이버 로그인 동의 이후 날아오는 응답
	@RequestMapping(value="/user/naverLogin/getToken")
	public String getNaverToken(@RequestParam("state") String state, @RequestParam("code") String code,
												HttpSession session, RedirectAttributes attr) {
		//세션에 저장해둔 상태토큰 가져오기
		String stateToken = (String) session.getAttribute("stateToken");
		//System.out.println("내가 생성한 상태 토큰 "+stateToken);
		//System.out.println("네이버가 생성한 상태 토큰 "+state);
		
		//저장해둔 상태토큰 값과 넘어온 state 값이 일치하는지 확인
		if(state.equals(stateToken) ) {				//일치하면
			//System.out.println("일치 성공");
		    //접근 토큰 발급
			String accessToken = service.getAccessToken(state,code);
			//회원정보 불러오기
			boolean result = service.getNaverUserInfo(accessToken, session);
			attr.addFlashAttribute("msg",result);
		}
		return "redirect:/";
	}
	
	//마이페이지 가는 요청
	@RequestMapping(value="/user/mypage")
	public String mypage() {
		return "/user/mypage";
	}
	
	//로그아웃 요청
	@RequestMapping(value="/user/logout",method=RequestMethod.GET)
	public String logout(HttpSession session) {
		
		//세션에서 userInfo 지우기
		session.removeAttribute("userNickname");
		session.removeAttribute("userImage");
		session.removeAttribute("userEmail");
		
		return "redirect:/";
	}
	
	//비밀번호 찾기 페이지 가기
	@RequestMapping(value="/user/pw/forget",method=RequestMethod.GET)
	public String pwforget() {
		return "/user/pwforget";
	}
}
