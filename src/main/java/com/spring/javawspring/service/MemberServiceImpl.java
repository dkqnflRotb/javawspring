package com.spring.javawspring.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.common.JavawspringProvide;
import com.spring.javawspring.dao.MemberDAO;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	MemberDAO memberDAO;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Override
	public MemberVO getMemberIdCheck(String mid) {
		return memberDAO.getMemberIdCheck(mid);
	}

	@Override
	public MemberVO getMemberNickCheck(String nickName) {
		return memberDAO.getMemberNickCheck(nickName);
	}

	@Override
	public int setMemberJoinOk(MultipartFile fName, MemberVO vo) {
		// 업로드된 사진을 서버 파일 시스템에 저장시켜준다.
		int res = 0;
		try {
			String oFileName = fName.getOriginalFilename();
			if(oFileName.equals("")) {
				vo.setPhoto("noimage.jpg");
			}
			else {
				
			
				UUID uid = UUID.randomUUID();  // 파일명이 중복되지 않게 설정하기위해 써준다.
				String saveFileName = uid + "_" + oFileName;
				
				JavawspringProvide ps = new JavawspringProvide();
				ps.writeFile(fName,saveFileName, "member");
				vo.setPhoto(saveFileName);
			}
			memberDAO.setMemberJoinOk(vo);
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return res;
	}

	@Override
	public void setMemberVisitProcess(MemberVO vo) {
		// 오늘 날짜 편집
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strNow = sdf.format(now);
		
		// 오늘 처음 방문시는 오늘 방문카운트(todayCnt)를 0으로 셋팅한다. 
		if(!vo.getLastDate().substring(0,10).equals(strNow)) {
			// memberDAO.setTodayCntUpdate(vo.getMid());
			vo.setTodayCnt(0);
		}
		
		int todayCnt = vo.getTodayCnt()+1;
		
		int nowTodayPoint = 0;
		if(vo.getTodayCnt() >= 5) {
			nowTodayPoint = vo.getPoint();
		}
		else {
			nowTodayPoint = vo.getPoint() + 10;
		}
		// 오늘 재방문이라면 '총방문수','오늘방문수','포인트' 누적처리
		memberDAO.setMemTotalUpdate(vo.getMid(), nowTodayPoint, todayCnt);
	}

	@Override
	public ArrayList<GuestVO> getGuestList(int startIndexNo, int pageSize) {
		return memberDAO.getGuestList(startIndexNo, pageSize);
	}

	@Override
	public int memberMidEmailcheck(String mid, String email) {
		return memberDAO.memberMidEmailcheck(mid, email);
	}

	@Override
	public void imsiPwd(String email, String mid) {
		UUID uid = UUID.randomUUID();
		
		try {
			String toMail = email;
			String title = "임시 비밀번호";
			String content = "";
			
			// 메일을 전송하기 위한 객체 : MimeMessage, MimeMessageHelper()
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			// 메일보관함에 회원이 보내온 메세지들을 모두 저장시킨다.
			messageHelper.setTo(toMail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);
			
			// 메세지 보관함의 내용(content)에 필요한 정보를 추가로 담아서 전송시킬수 있도록 한다.
			content = content.replace("\n","<br/>");
			content += "<br><hr><h3>CJ Green에서 보냅니다.</h3><hr><br>";
			content += "<p>임시비밀번호 : "+uid+"</p>";
			content += "<p>비밀번호를 번경해주세요</p>";
			content += "<hr>";
			
			messageHelper.setText(content,true);
			// 메일 전송하기
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		String imsiPwd = passwordEncoder.encode(uid.toString());
		memberDAO.imsiPwd(imsiPwd,mid);
	}

	@Override
	public int totTermRecCnt(String mid) {
		return memberDAO.totTermRecCnt(mid);
	}

	@Override
	public ArrayList<GuestVO> getTermGuestList(int startIndexNo, int pageSize, String mid) {
		return memberDAO.getTermGuestList(startIndexNo, pageSize, mid);
	}

	@Override
	public List<MemberVO> getMemberList(int startIndexNo, int pageSize, String part, String search) {
		return memberDAO.getMemberList(startIndexNo, pageSize, part, search);
	}

	@Override
	public void memberPwdUpdate(String mid, String pwd) {
		memberDAO.memberPwdUpdate(mid, pwd);
	}

	@Override
	public String getMemberIDSearch(String email, String name) {
		return memberDAO.getMemberIDSearch(email, name);
	}

	@Override
	public int memberDelete(String mid) {
		return memberDAO.memberDelete(mid);
	}
}
