package com.gmail.hyunyful.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gmail.hyunyful.dao.ReplyDao;
import com.gmail.hyunyful.domain.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao dao;
	
	@Autowired
	private DataSourceTransactionManager tm;		//트랜잭션에 필요한 애
	
	//댓글 목록
	@Override
	public List<Reply> list(int bno) {
		List<Reply> list = new ArrayList<>();
		
		list = dao.list(bno);
		
		//list를 돌면서 del 값이 1이면 content는 삭제된 댓글입니다 라고 설정
		for(Reply r:list) {
			if(r.getDel() == 1) {
				r.setContent("삭제된 댓글입니다");
			}
		}
		
		return list;
	}
	
	//댓글 개수
	@Override
	public int count(int bno) {
		int result = 0;
		
		result = dao.count(bno);
		
		return result;
	}

	//댓글 작성
	@Override
	public boolean insert(HttpServletRequest request) {
		boolean result = false;
		
		String nickname = (String)request.getSession().getAttribute("userNickname");			//닉네임은 세션에서 가져오기
		String content = request.getParameter("content");
		int bno = Integer.parseInt(request.getParameter("bno"));
		
		Reply reply = new Reply();
		reply.setBno(bno);
		reply.setContent(content);
		reply.setNickname(nickname);
		
		int r = dao.insert(reply);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

	//댓글 수정
	@Override
	public boolean update(HttpServletRequest request) {
		boolean result = false;
		
		int rno = Integer.parseInt(request.getParameter("rno"));
		String content = request.getParameter("content");
		
		Reply reply = new Reply();
		reply.setRno(rno);
		reply.setContent(content);
		
		int r = dao.update(reply);
		if(r>0) {
			result = true;
		}

		return result;
	}

	//댓글 삭제
	@Override
	public boolean delete(int rno) {
		boolean result = false;
		
		int r = dao.delete(rno);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

	//댓글 좋아요 누르기
	@Override
	public String good(HttpServletRequest request) {
		String msg = "";
		
		int rno = Integer.parseInt(request.getParameter("rno"));
		String nickname = (String)request.getSession().getAttribute("userNickname");
		
		//트랜잭션으로 해당 댓글에 해당 닉네임이 좋아요를 누른적 있는지 확인
		//기록이 없으면(null이면) 좋아요 등록, 있으면 좋아요 취소
		DefaultTransactionDefinition transaction = new DefaultTransactionDefinition();
		transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(transaction);
		
		try {
			//여기에 트랜잭션할 내용 작성
			
			//좋아요 누른적 있는지 확인부터
			Reply reply = new Reply();
			reply.setRno(rno);
			reply.setNickname(nickname);
			Reply resultReply = dao.gbcheck(reply);
			
			//기록이 없으면 좋아요 수행
			if(resultReply == null) {
				int goodResult = dao.good(rno);							//좋아요
				int goodLogResult = dao.goodLog(reply);			//로그 남기기
				msg = "success";
			}
			
			//기록이 있으면 
			if(resultReply != null) {
				//resultReply의 good값과 bad값 가져오기
				int goodval = resultReply.getGood();
				int badval = resultReply.getBad();
				
				//goodval의 값이 1이면 좋아요 취소
				if(goodval == 1) {
					int cancelGoodResult = dao.cancelGood(rno);		//취소
					int deleteLogResult = dao.deleteLog(reply);			//로그 삭제
					msg = "cancel";
				}
				//badval의 값이 1이면 이미 싫어요를 눌렀다고 출력
				else if(badval == 1) {
					msg = "alreadyBad";
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			tm.rollback(status);
			throw e;
		}
		
		tm.commit(status);
		return msg;
	}

	//댓글 싫어요
	@Override
	public String bad(HttpServletRequest request) {
		String msg = "";
		
		int rno = Integer.parseInt(request.getParameter("rno"));
		String nickname = (String)request.getSession().getAttribute("userNickname");
		
		//트랜잭션으로 해당 댓글에 해당 닉네임이 싫어요를 누른적 있는지 확인
		//기록이 없으면(null이면) 싫어요 등록, 있으면 싫어요 취소
		DefaultTransactionDefinition transaction = new DefaultTransactionDefinition();
		transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = tm.getTransaction(transaction);
		
		try {
			//여기에 트랜잭션할 내용 작성
			
			//싫어요 누른적 있는지 확인부터
			Reply reply = new Reply();
			reply.setRno(rno);
			reply.setNickname(nickname);
			Reply resultReply = dao.gbcheck(reply);
			
			//기록이 없으면 싫어요 수행
			if(resultReply == null) {
				int badResult = dao.bad(rno);								//좋아요
				int badLogResult = dao.badLog(reply);				//로그 남기기
				msg = "success";
			}
			
			//기록이 있으면 
			if(resultReply != null) {
				//resultReply의 good값과 bad값 가져오기
				int goodval = resultReply.getGood();
				int badval = resultReply.getBad();
				
				//badval의 값이 1이면 싫어요 취소
				if(badval == 1) {
					int cancelBadResult = dao.cancelBad(rno);			//취소
					int deleteLogResult = dao.deleteLog(reply);			//로그 삭제
					msg = "cancel";
				}
				//goodval의 값이 1이면 이미 좋아요를 눌렀다고 출력
				else if(goodval == 1) {
					msg = "alreadyGood";
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			tm.rollback(status);
			throw e;
		}

		tm.commit(status);
		return msg;
	}
}
