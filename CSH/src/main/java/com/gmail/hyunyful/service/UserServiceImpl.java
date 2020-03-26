package com.gmail.hyunyful.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gmail.hyunyful.dao.UserDao;
import com.gmail.hyunyful.domain.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao dao;
	
	//id 중복체크
	@Override
	public boolean idcheck(String id) {
		boolean result = false;
		
		//dao 호출
		User user = dao.idcheck(id);
		if(user == null) {
			result = true;
		}
		
		return result;
	}

	//회원가입
	@Override
	public boolean join(MultipartHttpServletRequest request) {
		boolean result = false;
		
		String id = request.getParameter("id"); 
		String pw = request.getParameter("pw"); 
		String gender = request.getParameter("gender");
		String year = request.getParameter("year"); 
		String month = request.getParameter("month"); 
		String day = request.getParameter("day");
		String type = request.getParameter("type");
		
		//gender 값에 따라서 남자/여자로 저장하기 위해서
		if("man".equals(gender)) {
			gender="남자";
		}else {
			gender="여자";
		}
		
		//파일 처리
		MultipartFile f=request.getFile("image");			//file 파라미터 값 가져오기
		String filename=id+"image";							//id+image로 유일무이한 이름 만들기
		//저장할 디렉토리 이름 만들기
		//프로젝트 내에 만드는게 좋음
		//실행하기 전에 프로젝트 내의 webapp(WebContent)/memberimage 디렉토리를 만들어야 함
		String path=request.getServletContext().getRealPath("/userimage");
		//업로드 하는 파일이 있으면 저장하고 그렇지 않으면 default.png 저장
		if(f.getOriginalFilename().length() > 0) {
			//파일 업로드
			File file=new File(path+"/"+filename);
			try {
				f.transferTo(file);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}else {
			filename="default.png";
		}
		
		//비밀번호 암호화
		pw = BCrypt.hashpw(pw, BCrypt.gensalt(10));
		
		//연,월,일 따로 받아온 값을 Date로 변환
		String dateform = year+"-"+month+"-"+day;
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Date birthday = new Date();
		try {
			birthday = fm.parse(dateform);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User user = new User();
		user.setId(id);
		user.setPw(pw);
		user.setGender(gender);
		user.setBirthday(birthday);
		user.setImage(filename);
		user.setType(type);
		
		//dao 호출
		int r = dao.join(user);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

}
