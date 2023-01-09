package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MemberVO;

public interface MemberDAO {

	public MemberVO getMemberIdCheck(@Param("mid") String mid);

	public MemberVO getMemberNickCheck(@Param("nickName") String nickName);

	public int setMemberJoinOk(@Param("vo") MemberVO vo);

	public void setMemTotalUpdate(@Param("mid") String mid,@Param("nowTodayPoint") int nowTodayPoint,@Param("todayCnt") int todayCnt);

	public int totRecCnt();

	public ArrayList<GuestVO> getGuestList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize);

	public int memberMidEmailcheck(@Param("mid") String mid, @Param("email") String email);

	public void imsiPwd(@Param("imsiPwd") String imsiPwd, @Param("mid") String mid);

	public int totTermRecCnt(@Param("mid") String mid);

	public ArrayList<GuestVO> getTermGuestList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize, @Param("mid") String mid);

	public List<MemberVO> getMemberList(@Param("startIndexNo") int startIndexNo, @Param("pageSize") int pageSize);

}
