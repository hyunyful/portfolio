package com.gmail.hyunyful.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmail.hyunyful.dao.BoardDao;
import com.gmail.hyunyful.domain.Board;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao dao;
	
	//글 작성
	@Override
	public boolean write(HttpServletRequest request) {
		boolean result = false;
		
		//파라미터 받기
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String writer = request.getParameter("writer");
		String email = (String)request.getSession().getAttribute("userEmail");		//얘는 세션에서 가져오기
		//board에 저장
		Board board = new Board();
		board.setTitle(title);
		board.setContent(content);
		board.setWriter(writer);
		board.setEmail(email);
		//dao호출
		int r = dao.write(board);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

	//글 목록
	@Override
	public List<Board> list() {
		List<Board> list = new ArrayList<>();
		
		list = dao.list();
		
		return list;
	}

	//내가 쓴 글
	@Override
	public List<Board> mine(String email) {
		List<Board> list = new ArrayList<>();
		
		list = dao.mine(email);
		
		return list;
	}

	//선택한 작성자의 글 모아보기
	@Override
	public List<Board> writerSelect(String writer) {
		List<Board> list = new ArrayList<>();
		
		list = dao.writerSelect(writer);
		
		return list;
	}

	//게시글 상세보기
	@Override
	public Board detail(int bno) {
		Board board = dao.detail(bno);
		return board;
	}

	//게시글 삭제
	@Override
	public boolean delete(int bno) {
		boolean result = false;
		
		int r = dao.delete(bno);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

	//게시글 수정
	@Override
	public boolean update(HttpServletRequest request) {
		boolean result = false;
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		Board board = new Board();
		board.setBno(bno);
		board.setTitle(title);
		board.setContent(content);
		
		int r = dao.update(board);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

}
