package com.gmail.hyunyful;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BoardController {

	@RequestMapping(value="/board/list",method=RequestMethod.GET)
	public String list() {
		return "/board/list";
	}
}
