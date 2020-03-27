package com.gmail.hyunyful.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gmail.hyunyful.domain.User;

@Repository
public class UserDao {

	@Autowired
	private SqlSession sqlSession;
	
	//id 중복검사
	public User idcheck(String id) {
		return sqlSession.selectOne("user.idcheck",id);
	}
	
	//회원가입
	public int join(User user) {
		return sqlSession.insert("user.join",user);
	}
	
	//일반 회원 로그인 처리
	public User login(String id) {
		return sqlSession.selectOne("user.login",id);
	}
}
