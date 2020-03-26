package com.gmail.hyunyful;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//스프링의 테스트 클래스로 설정
@RunWith(SpringJUnit4ClassRunner.class)
//스프링의 설정파일을 실행해서 bean을 ??????
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class MyTest {

	//동일한 자료형의 객체가 존재하면 주입받는 어노테이션
	//디비 연결확인
	@Autowired
	private DataSource dataSource;
		
	@Test
	public void DBtest() {
		try {
			Connection con = dataSource.getConnection();
			System.err.println(con);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	//mybatis 설정 확인
	@Autowired
	private SqlSession sqlSession;
		
	@Test
	public void MybatisTest() {
		System.err.println(sqlSession);
	}
}
