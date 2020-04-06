package com.gmail.hyunyful.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gmail.hyunyful.domain.Reply;

@Repository
public class ReplyDao {

	@Autowired
	private SqlSession sqlSession;
	
	//댓글 목록
	public List<Reply> list(int bno){
		return sqlSession.selectList("reply.list",bno);
	}
	
	//댓글 개수
	public int count(int bno) {
		return sqlSession.selectOne("reply.count", bno);
	}
	
	//댓글 작성
	public int insert(Reply reply) {
		return sqlSession.insert("reply.insert",reply);
	}
	
	//댓글 수정
	public int update(Reply reply) {
		return sqlSession.update("reply.update",reply);
	}
	
	//댓글 삭제
	public int delete(int rno) {
		return sqlSession.update("reply.delete",rno);
	}
	
	//댓글 좋아요/싫어요 확인
	public Reply gbcheck(Reply reply) {
		return sqlSession.selectOne("reply.gbCheck", reply);
	}
	
	//좋아요 누르기
	public int good(int rno) {
		return sqlSession.update("reply.good",rno);
	}
	
	//좋아요 누른 기록 남기기
	public int goodLog(Reply reply) {
		return sqlSession.insert("reply.goodLog", reply);
	}
	
	//좋아요 취소
	public int cancelGood(int rno) {
		return sqlSession.update("reply.cancelGood", rno);
	}
	
	//좋아요,싫어요 로그 지우기
	public int deleteLog(Reply reply) {
		return sqlSession.delete("reply.deleteLog", reply);
	}
	
	//싫어요 누르기
	public int bad(int rno) {
		return sqlSession.update("reply.bad", rno);
	}
	
	//싫어요 누른 기록 남기기
	public int badLog(Reply reply) {
		return sqlSession.insert("reply.badLog", reply);
	}
	
	//싫어요 취소
	public int cancelBad(int rno) {
		return sqlSession.update("reply.cancelBad", rno);
	}
}
