package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MemberVO;

public interface MemberService {

	public MemberVO getMemberIdCheck(String mid);

	public MemberVO getMemberNickCheck(String nickName);

	public int setMemberJoinOk(MultipartFile fName, MemberVO vo);

	public void setMemberVisitProcess(MemberVO vo);

	public ArrayList<GuestVO> getGuestList(int startIndexNo, int pageSize);

	public int memberMidEmailcheck(String mid, String email);

	public void imsiPwd(String email, String mid);

	public int totTermRecCnt(String mid);

	public ArrayList<GuestVO> getTermGuestList(int startIndexNo, int pageSize, String mid);

	public List<MemberVO> getMemberList(int startIndexNo, int pageSize, String part, String search);

	public void memberPwdUpdate(String mid, String pwd);

	public String getMemberIDSearch(String email, String name);

	public int memberDelete(String mid);

}
