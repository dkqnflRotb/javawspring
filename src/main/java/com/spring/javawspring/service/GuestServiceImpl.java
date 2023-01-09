package com.spring.javawspring.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.javawspring.dao.GuestDAO;
import com.spring.javawspring.vo.GuestVO;

@Service
public class GuestServiceImpl implements GuestService {	
	
	@Autowired    /* 인터페이스로만들면 Autowired를 쓰고 class로 만들면 안써도된다.(그냥 메서드생성하면됨)*/
	GuestDAO guestDAO;

	@Override
	public ArrayList<GuestVO> getGuestList(int startIndexNo, int pageSize) {
		
		return guestDAO.getGuestList(startIndexNo, pageSize);
	}

	@Override
	public void setGuestInput(GuestVO vo) {
		guestDAO.setGuestInput(vo);
	}

	@Override
	public int totRecCnt() {
		return guestDAO.totRecCnt();
	}

	@Override
	public void setguestDelete(int idx) {
		guestDAO.setguestDelete(idx);
	}
}
