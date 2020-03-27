package com.gmail.hyunyful;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
		
		return "redirect:/user/login";
	}
	
	//로그인 페이지 이동
	@RequestMapping(value="/user/login",method=RequestMethod.GET)
	public String login() {
		return "/user/login";
	}
	
	//로그인 처리
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public String login(HttpServletRequest request, RedirectAttributes attr) {
		//service 호출
		boolean result = service.login(request);
		//로그인에 성공하면
		if(result == true) {
			//메인 페이지로 리다이렉트
			System.err.println("로그인 성공");
			return "redirect:/";
		}else {
			//attr에 실패 메세지를 담아서 로그인 페이지로 리다이렉트
			attr.addFlashAttribute("loginResult",result);
			System.err.println("로그인 실패");
			return "redirect:/user/login";
		}
	}
	
	@RequestMapping(value="/user/kakaologin")
	public String getCode(@RequestParam("code") String code,RedirectAttributes attr) {
		//System.out.println("받은 코드의 값은 "+code);
		
		//서비스의 getToken() 호출
		String access_token = service.getToken(code);
		//System.out.println("얻은 토큰의 값은 "+access_token);
		
		//서비스의 유저정보 불러오는 메소드 호출
		Map<String,Object> map = service.getKakaoUserInfo(access_token);
		//String nickname = service.getKakaoUserInfo(access_token);
		//System.out.println("유저의 닉네임은 "+nickname);
		//attr.addFlashAttribute("userNickname",nickname);
		attr.addFlashAttribute("userInfo",map);
		
		//로그인에 성공하면 메인으로
		return "redirect:/";
	}
}
