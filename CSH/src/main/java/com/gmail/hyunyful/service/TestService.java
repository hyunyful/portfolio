package com.gmail.hyunyful.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gmail.hyunyful.dao.TsetDao;
import com.gmail.hyunyful.domain.Test;

@Service
public class TestService {

	@Autowired
	private TsetDao dao;
	
	@Autowired
	private DataSourceTransactionManager tm;
	
	public boolean insert(HttpServletRequest req) {
		boolean result = false;
		
		/*int aa = Integer.parseInt(req.getParameter("aa"));
		int bb = Integer.parseInt(req.getParameter("bb"));
		
		Test test = new Test();
		test.setAa(aa);
		test.setBb(bb);
		
		int r1 = dao.insert(test);
		System.out.println("r1은 "+r1);
		
		int r2 = dao.insert(test);
		System.out.println("r2는 "+r2);
		
		if(r1>0 && r2>0) {
			result = true;
		}*/
		
		
		 DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
		 def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		 TransactionStatus status = tm.getTransaction(def); 
		 try { 
			 int aa = Integer.parseInt(req.getParameter("aa")); 
			 int bb = Integer.parseInt(req.getParameter("bb"));
		 
			 Test t = new Test(); t.setAa(aa); t.setBb(bb);
		 
			 int r1 = dao.insert(t); 
			 if(r1>0) { 
				 System.out.println("r1은 "+r1); 
			 }
		 
			 int r2 = dao.insert(t); 
			 if(r2>0) { 
				 System.out.println("r2은 "+r2);
			 }
		 
			 if(r1>0 && r2>0) { 
				 result = true; 
			 } 
		 } catch (Exception e) {
			 e.printStackTrace(); 
			 tm.rollback(status); 
			 throw e; 
		 }
		 
		  tm.commit(status);
		 
		System.out.println("return전 result는 "+result);
		return result;
	}
}
