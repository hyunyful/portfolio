package com.gmail.hyunyful.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gmail.hyunyful.domain.Board;

@Repository
public class BoardDao {

	@Autowired
	private SqlSession sqlSession;
	
	//글 작성
	public int write(Board board) {
		return sqlSession.insert("board.write",board);
	}
	
	//글 목록
	public List<Board> list() {
		return sqlSession.selectList("board.list");
	}
	
	//내가 쓴 글만 가져오기
	public List<Board> mine(String email){
		return sqlSession.selectList("board.mine",email);
	}
	
	//선택한 작성자의 글 가져오기
	public List<Board> writerSelect(String writer){
		return sqlSession.selectList("board.writerSelect", writer);
	}
	
	//게시글 상세보기
	public Board detail(int bno) {
		return sqlSession.selectOne("board.detail",bno);
	}
	
	//게시글 삭제
	public int delete(int bno) {
		return sqlSession.update("board.delete",bno);
	}
	
	//게시글 수정
	public int update(Board board) {
		return sqlSession.update("board.update",board);
	}
	
	//최신글 5개 가져오기
	public List<Board> newest(){
		return sqlSession.selectList("board.newest");
	}
}
