package com.gmail.hyunyful.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gmail.hyunyful.domain.Board;

public interface BoardService {

	//글 작성
	public boolean write(HttpServletRequest request);
	
	//글 목록
	public List<Board> list();
	
	//내가 쓴 글
	public List<Board> mine(String email);
	
	//선택한 작성자의 글 모아보기
	public List<Board> writerSelect(String writer);
	
	//게시글 상세보기
	public Board detail(int bno);
	
	//게시글 삭제
	public boolean delete(int bno);
	
	//게시글 수정
	public boolean update(HttpServletRequest request);
	
	//최신글 5개 가져오기
	public List<Board> newest();
}
