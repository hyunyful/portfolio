package com.gmail.hyunyful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.hyunyful.domain.Reply;
import com.gmail.hyunyful.domain.User;
import com.gmail.hyunyful.service.ReplyService;
import com.gmail.hyunyful.service.UserService;

@RestController
public class JsonController {
	
	@Autowired
	private UserService userService;

	//이메일 중복검사
	//PathVariable은 파라미터 이름 없이 / 후 값만 넘어오는 경우
	//RequestParam은 ?id=id값 처럼 파라미터 이름과 함께 넘어오는 경우 사용
	@RequestMapping(value="/user/join/emailcheck",method=RequestMethod.GET) 
	public Map<String,Object> emailcheck(@RequestParam("email") String email){
		Map<String,Object> map = new HashMap<String,Object>();
		//System.out.println(email);
		//service 호출
		boolean result = userService.emailcheck(email);
		map.put("result", result);
		//System.out.println(result);
		return map;
	}
	
	//닉네임 중복검사
	@RequestMapping(value="/user/join/nicknamecheck",method=RequestMethod.GET)
	public Map<String,Object> nicknamecheck(@RequestParam("nickname") String nickname){
		Map<String,Object> map = new HashMap<>();
		
		boolean result = userService.nicknamecheck(nickname);
		map.put("result", result);
		
		return map;
	}
	
	//네이버 로그인 전에 상태토큰 가져오기(비동기)
	@RequestMapping(value="/user/naverLogin")
	public Map<String,Object> getNaverStateToken(HttpSession session) {
		Map<String,Object> map = new HashMap<>();
					
		//System.out.println("컴 인?");
			
		String stateToken = "";
					
		stateToken = userService.getStateToken();
		//System.out.println(stateToken);
		//map에 담아서 보내기
		map.put("stateToken",stateToken);
		//세션에 저장
		session.setAttribute("stateToken", stateToken);
					
		//return "";
		return map;
	}
	
	//비밀번호 재설정 전에 비밀번호 확인 요청
	@RequestMapping(value="/user/pwcheck",method=RequestMethod.POST)
	public Map<String,Object> pwcheck(HttpServletRequest request){	
		Map<String,Object> map = new HashMap<>();
		
		String msg = userService.pwcheck(request);
		map.put("result", msg);
		
		return map;
	}
	
}
