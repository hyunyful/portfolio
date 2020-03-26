package com.gmail.hyunyful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
