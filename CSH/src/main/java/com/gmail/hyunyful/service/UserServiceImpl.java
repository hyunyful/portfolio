package com.gmail.hyunyful.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	
	//이메일 중복체크
	@Override
	public boolean emailcheck(String email) {
		boolean result = false;
		
		//dao 호출
		User user = dao.emailcheck(email);
		if(user == null) {
			result = true;
		}
		
		return result;
	}
	
	//닉네임 중복검사
	@Override
	public boolean nicknamecheck(String nickname) {
		boolean result = false;
		
		User user = dao.nickname(nickname);
		if(user == null) {
			result = true;
		}
		
		return result;
	}

	//회원가입
	@Override
	public boolean join(MultipartHttpServletRequest request) {
		boolean result = false;
		
		String e = request.getParameter("e"); 
		String mail = request.getParameter("mail"); 
		String pw = request.getParameter("pw"); 
		String nickname = request.getParameter("nickname");
		String type = request.getParameter("type");		//기본 common
		int authority = Integer.parseInt(request.getParameter("authority"));		//기본 1
		
		//파일 처리
		MultipartFile f=request.getFile("image");			//file 파라미터 값 가져오기
		String filename=e+"image";							//id+image로 유일무이한 이름 만들기
		//저장할 디렉토리 이름 만들기
		//프로젝트 내에 만드는게 좋음
		//실행하기 전에 프로젝트 내의 webapp(WebContent)/memberimage 디렉토리를 만들어야 함
		String path=request.getServletContext().getRealPath("/resources/userimage");
		System.out.println("file path는 "+path);
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
		
		//따로따로 얻어온 e와 mail을 합쳐서 email만들기
		String email = e+"@"+mail;
		
		User user = new User();
		user.setEmail(email);
		user.setPw(pw);
		user.setNickname(nickname);
		user.setAuthority(authority);
		user.setImage(filename);
		user.setType(type);
				
		 //dao 호출 
		int r = dao.join(user); if(r>0) { result = true; }
		
		return result;
	}

	//일반 회원 로그인 처리
	@Override
	public boolean login(HttpServletRequest request) {
		boolean result = false;
		//파라미터(id와 pw) 받기
		String email = request.getParameter("email");
		String pw = request.getParameter("pw");
		
		//id로 정보 불러오기
		User user = dao.login(email);
		
		//세션에서 로그인 정보를 가진 키의 값을 삭제
		//저장하기 전에 이미 있는 세션이 있을 수도 있으니 초기화
		request.getSession().removeAttribute("userInfo");
				
		//데이터가 존재한다면
		if(user != null) {
			//비밀번호 확인
			if(BCrypt.checkpw(pw, user.getPw())) {
				//비밀번호가 일치하면 세션의 user에 사용자 정보 저장
				//비밀번호는 제외(보안)
				user.setPw(null);
				request.getSession().setAttribute("userInfo", user);
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
		//String refresh_token = "";		//리프레시 토큰을 받을 변수
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
			//refresh_token = data.getString("refresh_token");
			
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
	public boolean getKakaoUserInfo(String token, HttpSession session) {
		boolean b = true;
		
		String email = "";
		String image = "";
		String nickname = "";
		
		//기본 요청 주소
		String requestURI = "https://kapi.kakao.com/v2/user/me";	
		
		try {
			//요청 주소 생성
			URL url = new URL(requestURI);
			//연결
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			
			//전체 정보를 가져올때는 get
			//특정 정보만 지정해서 가져올 때는 post를 쓰는 모양..
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
			
			//가입을 위해 nickname, image, email을 추출
			//profile 라는 json 객체 가져오기	//얘는 json안의 json임
			JSONObject kakao_account = data.getJSONObject("kakao_account");
			email = kakao_account.getString("email");
			JSONObject profile = kakao_account.getJSONObject("profile");
			nickname = profile.getString("nickname");
			image = profile.getString("profile_image_url");

			if(image.length() == 0) {
				image = "default.png";
			}
			//이메일로 가입되어있는지 가입 여부 확인
			User u = dao.snsJoinCheck(email);
			//없으면 가입 진행
			if(u == null) {
				User user = new User();
				user.setEmail(email);
				user.setType("kakao");
				user.setAuthority(1);
				user.setImage(image);
				user.setNickname(nickname);			//비밀번호는 없으니까 null로 설정
				int r = dao.join(user);
				if(r>0) {
					System.out.println("로그인하고 가입도 완료");
				}
			}else {
				System.out.println("가입 이미 되어있당");
			}
			
			session.removeAttribute("userInfo");
			session.setAttribute("userInfo",data);
			br.close();
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		
		//세션에 혹시 값이 있을 수도 있으니까 먼저 지우고 저장
		session.removeAttribute("userEmail");
		session.removeAttribute("userImage");
		session.removeAttribute("userNickname");
		session.setAttribute("userEmail",email);
		session.setAttribute("userImage",image);
		session.setAttribute("userNickname",nickname);
		return b;
	}

	//네이버 로그인 전에 상태 토큰 받기
	@Override
	public String getStateToken() {
		String stateToken = "";
		
		SecureRandom random = new SecureRandom();
	    stateToken = new BigInteger(130, random).toString(32);
		
		return stateToken;
	}

	//네이버 접근토큰 받기
	@Override
	public String getAccessToken(String state, String code) {
		//System.out.println("접근 토큰 생성 시작");
		String accessToken = "";
		
		//요청 생성
		String uri = "https://nid.naver.com/oauth2.0/token?client_id=Pkyzx4PwdKj4LrNvkqXS";
		uri += "&client_secret=4P5TrrSsAx&grant_type=authorization_code";
		uri += "&state="+state+"&code="+code;
		
		try {
			//URL 연결
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			//데이터 읽기
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";				//받아온 데이터를 한 줄 읽을 변수
			String result = "";			//받아온 데이터를 차곡차곡 저장할 변수
			while(true) {
				line = br.readLine();		//한줄 읽기
				if(line == null) {			//없으면
					break;						//반복문 나가기
				}
				result += line;				//읽은게 있으면 result에 차곡차곡 저장
			}
			
			//json으로 만들기
			JSONObject data = new JSONObject(result);
			//System.out.println(data);
			accessToken = data.getString("access_token");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("접근 토큰 생성 끝! 반환");
		return accessToken;
	}

	//네이버 사용자 요청 받기
	@Override
	public boolean getNaverUserInfo(String accessToken, HttpSession session) {
		boolean b = true;
		
		String email = "";
		String image = "";
		String nickname = "";
		
		String uri = "https://openapi.naver.com/v1/nid/me";
		
		try {
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestProperty("Authorization", "Bearer "+accessToken);
			
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
			
			//결과를 json 객체로 변환
			JSONObject data = new JSONObject(result);
			//가입 되어 있는지 확인
			JSONObject response = data.getJSONObject("response");
			email = response.getString("email");
			nickname = response.getString("nickname");
			image = response.getString("profile_image");
			User u = dao.snsJoinCheck(email);
			//가입 안되어 있으면
			if(u == null) {
				System.out.println("가입 안되어있어서 가입한당");
				User user = new User();
				user.setEmail(email);
				user.setImage(image);
				user.setNickname(nickname);
				user.setType("naver");
				user.setAuthority(1);
				int r = dao.join(user);
				if(r>0) {
					System.out.println("가입 완료");
				}
			}else {
				System.out.println("가입 되어있당");
			}
			
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}

		//세션에 혹시 값이 있을 수도 있으니까 먼저 지우고 저장
		session.removeAttribute("userEmail");
		session.removeAttribute("userImage");
		session.removeAttribute("userNickname");
		session.setAttribute("userEmail",email);
		session.setAttribute("userImage",image);
		session.setAttribute("userNickname",nickname);
		return b;
	}
	
}
