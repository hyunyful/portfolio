package com.gmail.hyunyful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.hyunyful.domain.User;
import com.gmail.hyunyful.service.UserService;

@RestController
public class JsonController {
	
	@Autowired
	private UserService service;

	//id 중복검사
	//PathVariable은 파라미터 이름 없이 / 후 값만 넘어오는 경우
	//RequestParam은 ?id=id값 처럼 파라미터 이름과 함께 넘어오는 경우 사용
	@RequestMapping(value="/user/join/idcheck/{id}",method=RequestMethod.GET) 
	public Map<String,Object> idcheck(@PathVariable("id") String id){
		Map<String,Object> map = new HashMap<String,Object>();
			
		//service 호출
		boolean result = service.idcheck(id);
		map.put("result", result);
			
		return map;
	}
}
