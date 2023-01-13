package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.BoardService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.BoardReplyVO;
import com.spring.javawspring.vo.BoardVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	PageProcess pageProcess;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value = "/boardList", method=RequestMethod.GET)
	public String boardListGet(Model model,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(name="part", defaultValue = "", required = false) String part,
			@RequestParam(name="searchString", defaultValue = "", required = false) String searchString) {
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "board", part, searchString);
		
		List<BoardVO> vos = boardService.getBoardList(pageVO.getStartIndexNo() , pageSize, part, searchString);
		
		model.addAttribute("vos",vos);
		model.addAttribute("pageVO",pageVO);

		return "board/boardList";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.GET)
	public String boardInputGet(Model model, HttpSession session, int pag, int pageSize) {
		String mid = (String)session.getAttribute("sMid");
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		
		model.addAttribute("email",vo.getEmail());
		model.addAttribute("homePage",vo.getHomePage());
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		return "board/boardInput";
	}
	
	@RequestMapping(value = "/boardInput", method = RequestMethod.POST)
	public String boardInputPost(BoardVO vo) {
		// content에 이미지가 저장되어 있다면, 저장된 이미지만 골라서 /resources/data/board/폴더에 저장시켜준다.
		boardService.imgCheck(vo.getContent());
		
		// 이미지 복사작업이 끝나면, board폴더에 실제로 저장된 파일명을 DB에 저장시켜준다.(/resources/data/ckeditor/ ==>> /resources/data/board/)
		vo.setContent(vo.getContent().replace("data/ckeditor/", "data/ckeditor/board/"));
		int res = boardService.setBoardInput(vo);
		
		if(res == 1) return "redirect:/msg/boardInputOk";
		else return "redirect:/msg/boardInputNo";
	}
	
	// 게시물 폼
	@RequestMapping(value = "/boardContent", method = RequestMethod.GET)
	public String boardContentGet(Model model, String sMid, int partIdx, int pag, int pageSize) {

		int goodCheck = 0;
		// 계시물 idx랑 방문자아이디가 good2 DB에 있는지 확인함
		int goodMidCheck = boardService.getGoodCheck(partIdx, sMid,"board");
		if(goodMidCheck > 0) {
			// 값이 있으면 good2 DB에 goodCheck (1 : on  0 : off) 값을 가져옴
			goodCheck = boardService.getGoodCheckOk(partIdx, sMid, "board");
		}
		else {
			// 값이 없으면 good2 DB에 값 새로 추가
			boardService.inputGoodCheck(partIdx, sMid, "board");
			
			// 조회수 증가처리 (값이 없으면 처음 방문이니 조회수 증가해줌)
			boardService.setBoardReadNum(partIdx);
		}
		
		BoardVO vo = boardService.setBoardContent(partIdx);
		
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		model.addAttribute("goodCheck",goodCheck);
		
		// 이전글/ 다음글 가져오기
		ArrayList<BoardVO> pnVos = boardService.getPrevNext(partIdx);
		System.out.println("pnVos : " + pnVos);
		model.addAttribute("pnVos",pnVos);
		
		// 댓글 가져오기(replyVos)
		List<BoardReplyVO> replyVos = boardService.getBoardReply(partIdx);
		List<BoardReplyVO> dchecks = boardService.getDchecks(partIdx);
		System.out.println("dchecks = "+dchecks);
		
		model.addAttribute("replyVos",replyVos);
		model.addAttribute("dchecks",dchecks);
		
		return "board/boardContent";
	}
	
	// 좋아요 클릭
	@ResponseBody
	@RequestMapping(value = "/boardGoodCheck", method = RequestMethod.POST)
	public int boardGoodCheckPost(int partIdx, int su, String sMid) {
		// 좋아요 클릭
		boardService.setBoardGoodCheck(partIdx,sMid,su,"board");
		
		boardService.setBoardGoodUpDown(partIdx,sMid,su);
		
		return su;
	}
	
	// 게시글 삭제하기 ..
	@RequestMapping(value = "/boardDeleteOk", method = RequestMethod.GET)
	public String boardDeleteOkGet(int idx, int pag, int pageSize, Model model) {
		// 게시글에 사진이 존재한다면 서버에 있는 사진파일을 먼저 삭제한다.
		BoardVO vo = boardService.setBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(vo.getContent());
		
		// DB에 실제로 존재하는 게시글을 삭제처리한다.
		boardService.setBoardDeleteOk(idx);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		return "redirect:/msg/boardDeleteOk";
	}
	
	// 수정하기 폼 호출
	@RequestMapping(value="/boardUpdate", method = RequestMethod.GET)
	public String boardUpdateGet(Model model, int idx, int pag, int pageSize) {
		// 수정창으로 이동시에는 먼저 원본파일에 그림파일이 있다면, 현재폴더(board)의 그림파일을 ckeditor폴더로 복사시켜둔다.
		BoardVO vo = boardService.setBoardContent(idx);
		if(vo.getContent().indexOf("src=\"/") != -1) boardService.imgCheckUpdate(vo.getContent());
		
		model.addAttribute("vo",vo);
		model.addAttribute("pag",pag);
		model.addAttribute("pageSize",pageSize);
		
		
		return "board/boardUpdate";
	}
	
	// 변경된 내용 수정처리(그림포함) 
	@RequestMapping(value="/boardUpdate", method = RequestMethod.POST)
	public String boardUpdatePost(Model model, BoardVO vo, int pag, int pageSize) {
		// 수정된 자료가 원본자료와 완전히 동일하다면 수정할 필요가 없기에, DB에 저장된 원본 자료를 불러와서 비교해준다.
		BoardVO origVo = boardService.setBoardContent(vo.getIdx());
		
		// content의 내용이 조금이라도 변경된것이 있다면 아래 내용을 수행처리시킨다.
		if(!origVo.getContent().equals(vo.getContent())) {
			// 실제로 수정하기버튼을 클릭하게되면 기존의 board폴더에 저장된 현재 content의 그림파일을 모두를 삭제 시킨다.
			if(origVo.getContent().indexOf("src=\"/") != -1) boardService.imgDelete(origVo.getContent());
			
			// vo.getContent()에 들어있는 파일의 경로는 'ckeditor/board' 경로를 'ckeditor' 변경시켜줘야한다.ㅣ
			vo.setContent(vo.getContent().replace("/data/ckeditor/board/", "/data/ckeditor/"));
			
			// 앞의 모든준비가 끝나면, 파일을 처음 업로드한것과 같은 작업을 처리한다.
			// 이 작업은 처음 게시글을 올릴때의 파일본사 작업과 동일한 작업이다.
			boardService.imgCheck(vo.getContent());
			
			// 파일 업로드가 끝나면 다시 경로를 수정한다. 'ckeditor' 경로를 'ckeditor/board'변경시켜줘야한다.(즉, 변경된 vo.getContent()를 vo.setContent() 처리한다.)
			vo.setContent(vo.getContent().replace("/data/ckeditor/", "/data/ckeditor/board/"));
		}
		
		
		// 잘 정비된 vo를 DB에 Update시켜준다.
		boardService.setBoardUpdateOk(vo);
		
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize);
		
		return "redirect:/msg/boardUpdateOk";
		
	}
	
	// 댓글 달기
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput", method=RequestMethod.POST)
	public String boardReplyInputPost(BoardReplyVO replyVo) {
		int levelOrder = 0;
		int Dcheck=0;
		BoardReplyVO replyVo1 = boardService.getMaxLevelOrder(replyVo.getBoardIdx());
		System.out.println("replyVo1.getDcheck() = " +replyVo1.getDcheck());
		if(replyVo1.getMaxlevelOrder() != 0) levelOrder = replyVo1.getMaxlevelOrder() + 1;
		Dcheck = replyVo1.getDcheck() + 1;

		
		System.out.println("Dcheck = " + Dcheck);
		System.out.println("levelOrder = " + levelOrder);
		
		replyVo.setLevelOrder(levelOrder);
		replyVo.setDcheck(Dcheck);
		
		boardService.setBoardReplyInput(replyVo);
		
		return "1";
	}
	// 대댓글 달기
	@ResponseBody
	@RequestMapping(value = "/boardReplyInput2", method=RequestMethod.POST)
	public String boardReplyInput2Post(BoardReplyVO replyVo) {
		
		boardService.setLevelOrderPlusUpdate(replyVo);
		
		replyVo.setLevel(replyVo.getLevel()+1);
		replyVo.setLevelOrder(replyVo.getLevelOrder()+1);
		boardService.setBoardReplyInput2(replyVo);
		
		
		return "1";
	}
	
	// 댓글 삭제하기
	@ResponseBody
	@RequestMapping(value = "/boardReplyDeleteOk", method=RequestMethod.POST)
	public String boardReplyDeleteOkPost(int idx) {
		
		boardService.setBoardReplyDeleteOk(idx);
		
		return "1";
	}
	

}

