package com.gmail.hyunyful.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gmail.hyunyful.domain.Test;

@Repository
public class TsetDao {

	@Autowired
	private SqlSession sqlSession;
	
	public int insert(Test test) {
		return sqlSession.insert("test.insert",test);
	}
}
