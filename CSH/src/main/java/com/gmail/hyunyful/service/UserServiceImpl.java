package com.gmail.hyunyful.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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
		int authority = Integer.parseInt(request.getParameter("authority"));
		
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
		user.setAuthority(authority);
		
		//dao 호출
		int r = dao.join(user);
		if(r>0) {
			result = true;
		}
		
		return result;
	}

	//일반 회원 로그인 처리
	@Override
	public boolean login(HttpServletRequest request) {
		boolean result = false;
		//파라미터(id와 pw) 받기
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		//id로 정보 불러오기
		User user = dao.login(id);
		
		//세션에서 로그인 정보를 가진 키의 값을 삭제
		//저장하기 전에 이미 있는 세션이 있을 수도 있으니 초기화
		request.getSession().removeAttribute("user");
				
		//데이터가 존재한다면
		if(user != null) {
			//비밀번호 확인
			if(BCrypt.checkpw(pw, user.getPw())) {
				//비밀번호가 일치하면 세션의 user에 사용자 정보 저장
				//비밀번호는 제외(보안)
				user.setPw(null);
				request.getSession().setAttribute("user", user);
				//로그인 성공시 result는 true
				result=true;
			}
		}
		
		return result;
	}

	//카카오 로그인 토큰 받기
	@Override
	public String getToken(String code) {
		String access_token = "";		//토큰을 받을 변수
		String refresh_token = "";		//리프레시 토큰을 받을 변수
		String requestURI = "https://kauth.kakao.com/oauth/token";		//요청 주소
		
		try {
			//요청 주소 생성
			URL url = new URL(requestURI);
			//연결
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//post 방식으로 보내라고 했음
			con.setRequestMethod("POST");
			con.setDoOutput(true);		//뭐하는건지 아직 잘 모르겠음..찾아봤는데 모르겠음...
			
			//보내야 하는 필수 파라미터들을 스트림으로 전송
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
			//문자열을 저장할 객체
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=a4145c3d7c70775e9b0ab558aee4ee92");
			sb.append("&redirect_uri=http://localhost:8080/user/kakaologin");
			sb.append("&code="+code);
			bw.write(sb.toString());
			bw.flush();
			
			//결과 코드가 200이면 성공
			int responseCode = con.getResponseCode();
			//System.out.println("결과 코드는 "+responseCode);
			
			//얻어온 json타입의 response 메세지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";			//한 줄씩 읽은 것을 저장할 변수
			String result = "";		//line의 값을 계속 추가받을 변수-response 메세지가 됨
			//읽을게 있으면 계속 읽기
			while(true) {
				line = br.readLine();
				//읽을게 없으면 반복문 나가기
				if(line == null) {
					break;
				}
				//한줄씩 읽은 것을 result에 추가
				result += line;
			}
			//System.out.println("response 메세지는 "+result);
			
			//다운받은 문자열을 json 객체로 파싱
			JSONObject data = new JSONObject(result);
			access_token = data.getString("access_token");
			refresh_token = data.getString("refresh_token");
			
			//System.out.println("access_token은 "+access_token);
			//System.out.println("refresh_token은 "+refresh_token);
			
			//사용한 객체 닫아주기
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return access_token;
	}

	//카카오톡 로그인시 유저정보 가져오기
	@Override
	public Map<String,Object> getKakaoUserInfo(String token) {
		Map<String,Object> map = new HashMap<>();
		String nickname = "";
		
		//기본 요청 주소
		String requestURI = "https://kapi.kakao.com/v2/user/me";	
		
		try {
			//요청 주소 생성
			URL url = new URL(requestURI);
			//연결
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			
			//post로 보내라 했으니 메소드 설정
			con.setRequestMethod("GET");
			
			//header에 토큰 넣어 보내기
			//header에 넣는 메소드는 setRequestProperty
			con.setRequestProperty("Authorization", "Bearer "+token);
			
			//넘어온 데이터 읽기
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			String result = "";
			while(true) {
				line = br.readLine();
				if(line == null) {
					break;
				}
				result += line;
			}
			
			//json파싱
			JSONObject data = new JSONObject(result);
			System.out.println("넘어온 유저 정보는 "+data);
			//kakao_account 라는 json 객체 가져오기	//얘는 json안의 json임
			JSONObject kakao_account = data.getJSONObject("kakao_account");
			//kakao_account라는 json 안에 있는 nickname 꺼내기
			//여기서 이제 원하는 정보를 다 꺼내서 map에 넣으면 된다
			//kakao_account라는 json 안에 profile 이라는 json 안에 nickname이 있음
			JSONObject profile = kakao_account.getJSONObject("profile");
			nickname = profile.getString("nickname");
			
			//map에 저장
			map.put("userInfo", data);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//return nickname;
		return map;
	}

	
}
