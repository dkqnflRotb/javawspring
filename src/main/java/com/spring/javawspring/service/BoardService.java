package com.spring.javawspring.service;

import java.util.ArrayList;
import java.util.List;

import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;

public interface BoardService {

	public List<BoardVO> getBoardList(int startIndexNo, int pageSize, String part, String searchString);

	public int setBoardInput(BoardVO vo);

	public BoardVO setBoardContent(int idx);

	public void setBoardReadNum(int partIdx);

	public int getGoodCheck(int partIdx, String sMid, String part);

	public int setBoardGoodCheck(int partIdx, String sMid, int su, String part);

	public int getGoodCheckOk(int partIdx, String sMid, String part);

	public void inputGoodCheck(int partIdx, String sMid, String part);

	public void setBoardGoodUpDown(int partIdx, String sMid, int su);

	public ArrayList<BoardVO> getPrevNext(int partIdx);

	public void imgCheck(String content);

	public void setBoardDeleteOk(int idx);

	public void imgDelete(String content);

	public void imgCheckUpdate(String content);

	public void setBoardUpdateOk(BoardVO vo);

	public void setBoardReplyInput(BoardReplyVO replyVo);

	public List<BoardReplyVO> getBoardReply(int partIdx);

	public void setBoardReplyDeleteOk(int idx);

	public BoardReplyVO getMaxLevelOrder(int boardIdx);

	public void setBoardReplyInput2(BoardReplyVO replyVo);

	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo);

	public List<BoardReplyVO> getDchecks(int partIdx);

}
