package com.spring.javawspring.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;

public interface BoardDAO {

	public int totRecCnt(@Param("part") String part, @Param("searchString") String searchString);

	public List<BoardVO> getBoardList(@Param("startIndexNo") int startIndexNo,@Param("pageSize") int pageSize, @Param("part") String part, @Param("searchString") String searchString);

	public int setBoardInput(@Param("vo") BoardVO vo);

	public BoardVO setBoardContent(@Param("idx") int idx);

	public void setBoardReadNum(@Param("partIdx") int partIdx);

	public int getGoodCheck(@Param("partIdx") int partIdx, @Param("sMid") String sMid, @Param("part") String part);

	public int setBoardGoodCheck(@Param("partIdx") int partIdx, @Param("sMid") String sMid, @Param("su") int su, @Param("part") String part);

	public int getGoodCheckOk(@Param("partIdx") int partIdx, @Param("sMid") String sMid, @Param("part") String part);

	public void inputGoodCheck(@Param("partIdx") int partIdx, @Param("sMid") String sMid, @Param("part") String part);

	public void setBoardGoodUpDown(@Param("partIdx") int partIdx, @Param("sMid") String sMid, @Param("su") int su);

	public ArrayList<BoardVO> getPrevNext(@Param("partIdx") int partIdx);

	public void setBoardDeleteOk(@Param("idx") int idx);

	public void setBoardUpdateOk(@Param("vo") BoardVO vo);

	public void setBoardReplyInput(@Param("replyVo") BoardReplyVO replyVo);

	public List<BoardReplyVO> getBoardReply(@Param("partIdx") int partIdx);

	public void setBoardReplyDeleteOk(@Param("idx") int idx);

	public BoardReplyVO getMaxLevelOrder(@Param("boardIdx") int boardIdx);

	public void setBoardReplyInput2(@Param("replyVo") BoardReplyVO replyVo);

	public void setLevelOrderPlusUpdate(@Param("replyVo") BoardReplyVO replyVo);

	public List<BoardReplyVO> getDchecks(@Param("partIdx") int partIdx);
	
}
