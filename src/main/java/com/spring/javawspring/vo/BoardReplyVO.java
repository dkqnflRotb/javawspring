package com.spring.javawspring.vo;

import lombok.Data;

@Data
public class BoardReplyVO {
	private int idx;
	private int boardIdx;
	private String mid;
	private String nickName;
	private String wDate;
	private String hostIp;
	private String content;
	private int level;
	private int levelOrder;
	private int dcheck;
	
	private int maxlevelOrder;
	private int maxlevel;
	
	private int dcheckSu;	
	
	
	
}
