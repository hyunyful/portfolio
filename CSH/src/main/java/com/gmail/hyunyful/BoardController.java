package com.gmail.hyunyful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gmail.hyunyful.domain.Board;
import com.gmail.hyunyful.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;

	//게시글 목록 가기
	@RequestMapping(value="/board/list",method=RequestMethod.GET)
	public String list(Model model) {
		List<Board> list = service.list();
		model.addAttribute("list",list);
		
		return "/board/list";
	}
	
	//게시글 작성 페이지 가기
	@RequestMapping(value="/board/write",method=RequestMethod.GET)
	public String writePage(HttpServletRequest request) {
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
		
		return "/board/write";
	}
	
	//게시글 작성 
	@RequestMapping(value="/board/write",method=RequestMethod.POST)
	public String write(HttpServletRequest request, RedirectAttributes attr) {
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
		
		boolean result = service.write(request);
		attr.addFlashAttribute("write",result);
			
		return "redirect:/board/list";
	}
	
	//내가 쓴 글 모아보기
	@RequestMapping(value="/board/myList/{email:.+}")
	public String myList(@PathVariable String email, Model model, HttpServletRequest request) {
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
		
		List<Board> list = service.mine(email);
		model.addAttribute("myList",list);
		
		return "/board/myList";
	}
	
	//선택한 작성자의 글 모아보기
	@RequestMapping(value="/board/list/{writer}")
	public String writerSelect(@PathVariable String writer, Model model) {
		List<Board> list = service.writerSelect(writer);
		model.addAttribute("writerList",list);
		model.addAttribute("writer",writer);
		
		return "/board/writerList";
	}
	
	//게시글 상세보기
	@RequestMapping(value="/board/detail/{bno}")
	public String detail(@PathVariable int bno, Model model, HttpServletRequest request) {
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
		
		Board board = service.detail(bno);
		model.addAttribute("detail",board);
		
		return "/board/detail";
	}
	
	//게시글 삭제
	@RequestMapping(value="/board/delete/{bno}")
	public String delete(@PathVariable int bno,RedirectAttributes attr, HttpServletRequest request) {
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
		
		boolean result = service.delete(bno);
		//실패한 경우
		if(result == false) {
			attr.addFlashAttribute("delete",result);
			return "redirect:/board/delete/"+bno;
		}
		
		return "redirect:/board/list";
	}
	
	//게시글 수정 페이지 가기
	@RequestMapping(value="/board/update/{bno}",method=RequestMethod.GET)
	public String update(@PathVariable int bno,Model model, HttpServletRequest request) {
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
		
		Board board = service.detail(bno);
		model.addAttribute("detail",board);
		
		return "/board/update";
	}
	
	//게시글 수정
	@RequestMapping(value="/board/update",method=RequestMethod.POST)
	public String update(RedirectAttributes attr,HttpServletRequest request) {
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
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		boolean result = service.update(request);
		//실패하면
		if(result == false) {
			attr.addFlashAttribute("update",result);
			return "redirect:/board/update/"+bno;
		}
		
		return "redirect:/board/detail/"+bno;
	}
}
