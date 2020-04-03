package com.gmail.hyunyful.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gmail.hyunyful.domain.User;

@Repository
public class UserDao {

	@Autowired
	private SqlSession sqlSession;
	
	//이메일 중복검사
	public User emailcheck(String email) {
		return sqlSession.selectOne("user.emailcheck",email);
	}
	
	//닉네임 중복검사
	public User nickname(String nickname) {
		return sqlSession.selectOne("user.nicknamecheck",nickname);
	}
	
	//회원가입
	public int join(User user) {
		return sqlSession.insert("user.join",user);
	}
	
	//일반 회원 로그인 처리
	public User login(String email) {
		return sqlSession.selectOne("user.login",email);
	}
	
	//sns로그인 시 회원정보가 있는지 확인
	public User snsJoinCheck(String email) {
		return sqlSession.selectOne("user.snsJoinCheck",email);
	}
	
	//비밀번호 재설정
	public int resetPw(User user) {
		return sqlSession.update("user.resetPw",user);
	}
}
