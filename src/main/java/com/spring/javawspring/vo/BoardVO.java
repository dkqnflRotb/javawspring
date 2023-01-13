package com.spring.javawspring.vo;

import lombok.Data;

@Data
public class BoardVO {
	private int idx;
	private String nickName;;
	private String title;
	private String email;
	private String homePage;
	private String content;
	private String wDate;
	private String hostIp;
	private int readNum;
	private int good ;
	private String mid;
	
	//DB에 없는 필드를 vo에 추가
	private int day_diff; //날짜 차이 계산 필드 (1일차이 계산필드)
	private int hour_diff;
	
	// 이전글 / 다음글을 위한 변수 설정
	private int preIdx;
	private int nextIdx;
	private String preTitle;
	private String nextTitle;

	// 댓글의 갯수를 저장하기 위한 변수
	private int replyCnt;

	
}
