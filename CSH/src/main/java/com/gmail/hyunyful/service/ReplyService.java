package com.gmail.hyunyful.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gmail.hyunyful.domain.Reply;

public interface ReplyService {

	//댓글 목록
	public List<Reply> list(int bno);
	
	//댓글 개수
	public int count(int bno);
	
	//댓글 작성
	public boolean insert(HttpServletRequest request);
	
	//댓글 수정
	public boolean update(HttpServletRequest request);
	
	//댓글 삭제
	public boolean delete(int rno);
	
	//댓글 좋아요 누르기
	public String good(HttpServletRequest request);
	
	//댓글 싫어요 누르기
	public String bad(HttpServletRequest request);
}
