package com.gmail.hyunyful;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.hyunyful.domain.Reply;
import com.gmail.hyunyful.service.ReplyService;

//댓글은 거의 다 비동기로 이루어지므로 RestController로 진행
@RestController
public class ReplyController {

	@Autowired
	private ReplyService replyService;

	//댓글 목록
	@RequestMapping(value="/reply/list/{bno}")
	public Map<String,Object> replyList(@PathVariable int bno){
		Map<String,Object> map = new HashMap<>();
		
		List<Reply> list = replyService.list(bno);
		map.put("replyList", list);
		
		return map;
	}
	
	//댓글 작성
	@RequestMapping(value="/reply/insert",method=RequestMethod.POST)
	public Map<String,Object> replyInsert(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		
		boolean result = replyService.insert(request);
		map.put("replyInsert", result);
		
		return map;
	}
	
	//댓글 수정
	@RequestMapping(value="/reply/update",method=RequestMethod.POST)
	public Map<String,Object> replyUpdate(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		
		boolean result = replyService.update(request);
		map.put("replyUpdate", result);
		
		return map;
	}
	
	//댓글 삭제
	@RequestMapping(value="/reply/delete/{rno}",method=RequestMethod.GET)
	public Map<String,Object> delete(@PathVariable int rno){
		Map<String,Object> map = new HashMap<>();
		
		boolean result = replyService.delete(rno);
		map.put("replyDelete", result);
		
		return map;
	}
	
	//좋아요 요청
	@RequestMapping(value="/reply/good")
	public Map<String,Object> good(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		
		String msg = replyService.good(request);
		map.put("replyGood", msg);
		
		return map;
	}
	
	//싫어요 요청
	@RequestMapping(value="/reply/bad")
	public Map<String,Object> bad(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		
		String msg = replyService.bad(request);
		map.put("replyBad", msg);
		
		return map;
	}
	
	//댓글 개수 요청
	@RequestMapping(value="/reply/count/{bno}")
	public Map<String,Object> count(@PathVariable int bno){
		Map<String,Object> map = new HashMap<>();
		
		int result = replyService.count(bno);
		map.put("replyCount", result);
		
		return map;
	}
}
