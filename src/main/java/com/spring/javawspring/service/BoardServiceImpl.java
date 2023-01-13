package com.spring.javawspring.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.javawspring.dao.BoardDAO;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDAO boardDAO;

	@Override
	public List<BoardVO> getBoardList(int startIndexNo, int pageSize, String part, String searchString) {
		return boardDAO.getBoardList(startIndexNo, pageSize, part, searchString);
	}

	@Override
	public int setBoardInput(BoardVO vo) {
		return boardDAO.setBoardInput(vo);
	}

	@Override
	public BoardVO setBoardContent(int idx) {
		return boardDAO.setBoardContent(idx);
	}

	@Override
	public void setBoardReadNum(int partIdx) {
		boardDAO.setBoardReadNum(partIdx);
	}

	@Override
	public int getGoodCheck(int partIdx, String sMid, String part) {
		return boardDAO.getGoodCheck(partIdx, sMid, part);
	}

	@Override
	public int setBoardGoodCheck(int partIdx, String sMid, int su, String part) {
		return boardDAO.setBoardGoodCheck(partIdx, sMid, su, part);
	}

	@Override
	public int getGoodCheckOk(int partIdx, String sMid, String part) {
		return boardDAO.getGoodCheckOk(partIdx, sMid, part);
	}

	@Override
	public void inputGoodCheck(int partIdx, String sMid, String part) {
		boardDAO.inputGoodCheck(partIdx, sMid, part);
	}

	@Override
	public void setBoardGoodUpDown(int partIdx, String sMid, int su) {
		boardDAO.setBoardGoodUpDown(partIdx, sMid, su);
	}

	@Override
	public ArrayList<BoardVO> getPrevNext(int partIdx) {
		return boardDAO.getPrevNext(partIdx);
	}

	@Override
	public void imgCheck(String content) {
		
		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다. (src="/________~~)
		if(content.indexOf("src=\"/") == -1) return;
		//		0		  1	        2         3         4         5         6         7         8         9		  	
		//	    0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
		// <img src="/javawspring/data/ckeditor/2301111615_제네시스_G70.jpg" style="height:480px; width:900px" />

		// request 를 사용하기위해 써준다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/");
		
		int position =32;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = uploadPath + "board/" +imgFile;
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에 파일을 복사하고자 한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
		
	}

	private void fileCopyCheck(String origFilePath, String copyFilePath) {
		File origFile = new File(origFilePath);
		File copyFile = new File(copyFilePath);
		
		try {
			FileInputStream fis = new FileInputStream(origFile);
			FileOutputStream fos = new FileOutputStream(copyFile);
			
			byte[] buffer = new byte[2048];
			
			int cnt = 0;
			while((cnt = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, cnt);
			}
			
			fos.flush();
			fos.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBoardDeleteOk(int idx) {
		boardDAO.setBoardDeleteOk(idx);
	}

	@Override
	public void imgDelete(String content) {
		//		0		  1	        2         3         4         5         6         7         8         9		  	
		//	    0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
		// <img src="/javawspring/data/ckeditor/board/2301111615_제네시스_G70.jpg" style="height:480px; width:900px" />

		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다. (src="/________~~)
		if(content.indexOf("src=\"/") == -1) return;

		// request 를 사용하기위해 써준다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/");
		
		int position =38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));  //그림파일명 꺼내오기

			String origFilePath = uploadPath + imgFile;
			
			fileDelete(origFilePath); // board폴더에 파일을 삭제한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}

	private void fileDelete(String origFilePath) {
		File delFile = new File(origFilePath);
		if(delFile.exists()) delFile.delete();
	}

	@Override
	public void imgCheckUpdate(String content) {
		// content안에 그림파일이 존재할때만 작업을 수행할 수 있도록 한다. (src="/________~~)
		//		0		  1	        2         3         4         5         6         7         8         9		  	
		//	    0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
		// <img src="/javawspring/data/ckeditor/board/2301111615_제네시스_G70.jpg" style="height:480px; width:900px" />
		// <img src="/javawspring/data/ckeditor/2301111615_제네시스_G70.jpg" style="height:480px; width:900px" />
		if(content.indexOf("src=\"/") == -1) return;

		// request 를 사용하기위해 써준다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		
		//board에있는 파일을 가져가기위해
		String uploadPath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/board/"); 
		
		int position =38;
		String nextImg = content.substring(content.indexOf("src=\"/") + position);
		
		boolean sw = true;
		
		while(sw) {
			String imgFile = nextImg.substring(0, nextImg.indexOf("\""));
			
			String origFilePath = uploadPath + imgFile;
			String copyFilePath = request.getSession().getServletContext().getRealPath("/resources/data/ckeditor/"+imgFile);
			
			fileCopyCheck(origFilePath, copyFilePath); // board폴더에서 > ckeditor 파일로 복사하고자 한다.
			
			if(nextImg.indexOf("src=\"/") == -1) {
				sw = false;
			}
			else {
				nextImg = nextImg.substring(nextImg.indexOf("src=\"/") + position);
			}
		}
	}

	@Override
	public void setBoardUpdateOk(BoardVO vo) {
		boardDAO.setBoardUpdateOk(vo);
		
	}

	@Override
	public void setBoardReplyInput(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput(replyVo);		
	}

	@Override
	public List<BoardReplyVO> getBoardReply(int partIdx) {
		return boardDAO.getBoardReply(partIdx);
	}

	@Override
	public void setBoardReplyDeleteOk(int idx) {
		boardDAO.setBoardReplyDeleteOk(idx);
	}

	@Override
	public BoardReplyVO getMaxLevelOrder(int boardIdx) {
		return boardDAO.getMaxLevelOrder(boardIdx);
	}

	@Override
	public void setBoardReplyInput2(BoardReplyVO replyVo) {
		boardDAO.setBoardReplyInput2(replyVo);
		
	}

	@Override
	public void setLevelOrderPlusUpdate(BoardReplyVO replyVo) {
		boardDAO.setLevelOrderPlusUpdate(replyVo);
	}

	@Override
	public List<BoardReplyVO> getDchecks(int partIdx) {
		return boardDAO.getDchecks(partIdx);
	}
}
