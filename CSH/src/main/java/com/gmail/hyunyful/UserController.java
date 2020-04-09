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
		
		//세션의 uri 라는 키에 저장된 uri가 있다면 그리로 리다이렉트
		if(request.getSession().getAttribute("uri") != null) {
			String uri = (String)request.getSession().getAttribute("uri");
			request.getSession().removeAttribute("uri");
			
			return "redirect:"+ uri;
		}
		
		return "redirect:/";
	}
	
	//카카오 로그인
	@RequestMapping(value="/user/kakaologin")
	public String getCode(@RequestParam("code") String code, HttpServletRequest request, RedirectAttributes attr) {		
		//세션 얻어오기
		HttpSession session = request.getSession();
		
		//서비스의 getToken() 호출
		String access_token = service.getToken(code);
		
		//서비스의 유저정보 불러오는 메소드 호출
		boolean result = service.getKakaoUserInfo(access_token, session);
		attr.addFlashAttribute("msg",result);
		
		//세션의 uri 라는 키에 저장된 uri가 있다면 그리로 리다이렉트
		if(request.getSession().getAttribute("uri") != null) {
			String uri = (String)request.getSession().getAttribute("uri");
			request.getSession().removeAttribute("uri");
			
			return "redirect:"+ uri;
		}
		
		//로그인에 성공하면 메인으로
		return "redirect:/";
	}

	//네이버 로그인 동의 이후 날아오는 응답
	@RequestMapping(value="/user/naverLogin/getToken")
	public String getNaverToken(@RequestParam("state") String state, @RequestParam("code") String code,
												HttpServletRequest request, RedirectAttributes attr) {
		//세션 가져오기
		HttpSession session = request.getSession();
		
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
		
		//세션의 uri 라는 키에 저장된 uri가 있다면 그리로 리다이렉트
		if(request.getSession().getAttribute("uri") != null) {
			String uri = (String)request.getSession().getAttribute("uri");
			request.getSession().removeAttribute("uri");
			
			return "redirect:"+ uri;
		}
		
		return "redirect:/";
	}
	
	//마이페이지 가는 요청
	@RequestMapping(value="/user/mypage")
	public String mypage(HttpServletRequest request) {
		//로그인이 안되어 있으면
		if(request.getSession().getAttribute("userEmail") == null) {
			//현재의 uri를 세션에 저장(/board/wirte)
			//request.getRequestURI() 는 포트번호 이후의 경로를 반환 (String 형) ex./board/write
			//request.getRequestURL() 은 http부터 끝까지의 경로를 반환 (StringBuffer 형) ex.http://localhost:8080/board/write 
			String uri = request.getRequestURI();
			request.getSession().setAttribute("uri",uri); 
			//로그인 페이지가 없으므로 메인페이지로 리다이렉트
			return "redirect:/";
		}
		
		return "/user/mypage";
	}
	
	//로그아웃 요청
	@RequestMapping(value="/user/logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		//로그인이 안되어 있으면
		if(request.getSession().getAttribute("userEmail") == null) {
			//현재의 uri를 세션에 저장(/board/wirte)
			//request.getRequestURI() 는 포트번호 이후의 경로를 반환 (String 형) ex./board/write
			//request.getRequestURL() 은 http부터 끝까지의 경로를 반환 (StringBuffer 형) ex.http://localhost:8080/board/write 
			String uri = request.getRequestURI();
			request.getSession().setAttribute("uri",uri); 
			//로그인 페이지가 없으므로 메인페이지로 리다이렉트
			return "redirect:/";
		}
		
		//세션 가져오기
		HttpSession session = request.getSession();
		
		//세션에서 userInfo 지우기
		session.removeAttribute("userNickname");
		session.removeAttribute("userImage");
		session.removeAttribute("userEmail");
		session.removeAttribute("userType");
		
		return "redirect:/";
	}
	
	//비밀번호 재설정 페이지 가기
	@RequestMapping(value="/user/pwreset",method=RequestMethod.GET)
	public String pwforget(HttpServletRequest request) {
		//로그인이 안되어 있으면
		if(request.getSession().getAttribute("userEmail") == null) {
			//현재의 uri를 세션에 저장(/board/wirte)
			//request.getRequestURI() 는 포트번호 이후의 경로를 반환 (String 형) ex./board/write
			//request.getRequestURL() 은 http부터 끝까지의 경로를 반환 (StringBuffer 형) ex.http://localhost:8080/board/write 
			String uri = request.getRequestURI();
			request.getSession().setAttribute("uri",uri); 
			//로그인 페이지가 없으므로 메인페이지로 리다이렉트
			return "redirect:/";
		}
		
		return "/user/pwreset";
	}
	
	//비밀번호 재설정하기
	@RequestMapping(value="/user/pwreset",method=RequestMethod.POST)
	public String pwforget(HttpServletRequest request, RedirectAttributes attr) {
		//로그인이 안되어 있으면
		if(request.getSession().getAttribute("userEmail") == null) {
			//현재의 uri를 세션에 저장(/board/wirte)
			//request.getRequestURI() 는 포트번호 이후의 경로를 반환 (String 형) ex./board/write
			//request.getRequestURL() 은 http부터 끝까지의 경로를 반환 (StringBuffer 형) ex.http://localhost:8080/board/write 
			String uri = request.getRequestURI();
			request.getSession().setAttribute("uri",uri); 
			//로그인 페이지가 없으므로 메인페이지로 리다이렉트
			return "redirect:/";
		}
		
		boolean result = service.resetPw(request);
		attr.addFlashAttribute("resetPw",result);
		
		return "redirect:/";
	}
}
