package com.spring.javawspring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.GuestVO;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired //생성하는 개념 (자주써서 써줌)
	PageProcess pageProcess;
	
	@RequestMapping(value = "/memberLogin", method=RequestMethod.GET)
	public String memberLoginGet(HttpServletRequest request) {
		// 로그인폼 호출시에 기존에 저장된 쿠키가 있으면 불러와서 mid에 담아서 넘겨준다.
		Cookie[] cookies = request.getCookies();
		for(int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals("cMid")) {
				request.setAttribute("mid", cookies[i].getValue());
				break;
			}
		}
		return "member/memberLogin";
	}
	
	@RequestMapping(value = "/memberLogin", method=RequestMethod.POST)
	public String memberLoginPost(HttpServletRequest request,HttpServletResponse response, HttpSession session,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pwd", defaultValue = "", required = false) String pwd,
			@RequestParam(name="idCheck", defaultValue = "", required = false) String idCheck) {
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null && passwordEncoder.matches(pwd, vo.getPwd()) && !vo.getUserDel().equals("NO")) {
			// 회원 인증처리된 경우 수행할 내용? strLevel처리, session에 필요한 자료를 저장, 쿠키값 처리, 그날 방문자수 1 증가(방문포인트도 증가), ..
			String strLevel = "";
			if(vo.getLevel() == 0) strLevel = "관리자";
			else if(vo.getLevel() == 1) strLevel = "운영자";
			else if(vo.getLevel() == 2) strLevel = "우수회원";
			else if(vo.getLevel() == 3) strLevel = "정회원";
			else if(vo.getLevel() == 4) strLevel = "준회원";
			
			session.setAttribute("sStrLevel", strLevel);
			session.setAttribute("sLevel", vo.getLevel());
			session.setAttribute("sMid", mid);
			session.setAttribute("sNickName", vo.getNickName());
			
			if(idCheck.equals("on")) {
				Cookie cookie = new Cookie("cMid", mid);
				cookie.setMaxAge(60*60*24*7);
				response.addCookie(cookie);
			}
			else {
				Cookie[] cookies = request.getCookies();
				for(int i=0; i<cookies.length; i++) {
					if(cookies[i].getName().equals("cMid")) {
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
						break;
					}
				}
			}
			
			// 로그인한 사용자의 오늘 방문횟수(포인트) 누적...
			memberService.setMemberVisitProcess(vo);
			
			return "redirect:/msg/memberLoginOk?mid="+mid;
		}
		else {
			return "redirect:/msg/memberLoginNo";
		}
		
	}
	
	@RequestMapping(value = "/memberMain", method=RequestMethod.GET)
	public String memberMainGet(Model model, HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		model.addAttribute("vo",vo);
		
		return "member/memberMain";
	}
	@RequestMapping(value = "/memberLogout", method=RequestMethod.GET)
	public String memberLogoutGet(HttpSession session) {
		String mid = (String) session.getAttribute("sMid");
		
		session.invalidate();
		
		return "redirect:/msg/memberLogout?mid="+mid;
	}
	
	// 회원가입폼
	@RequestMapping(value = "/memberJoin", method=RequestMethod.GET)
	public String memberJoinGet() {
		
		return "member/memberJoin";
	}
	
	// 회원가입처리
	@RequestMapping(value = "/memberJoin", method=RequestMethod.POST)
	public String memberJoinPost(MemberVO vo) {
		//System.out.println("memberVo : " + vo);
		// 아이디 체크
		if(memberService.getMemberIdCheck(vo.getMid()) != null) {
			return "redirect:/msg/memberIdCheckNo";
		}
		// 닉네임 중복 체크
		if(memberService.getMemberNickCheck(vo.getNickName()) != null) {
			return "redirect:/msg/memberNickCheckNo";
		}
		
		// 비밀번호 암호화(BCryptPasswordEncoder)
		vo.setPwd(passwordEncoder.encode(vo.getPwd()));
		
		// 체크가 완료되면 vo에 담긴 자료를 DB에 저장시켜준다. (회원 가입)
		int res = memberService.setMemberJoinOk(vo);
		System.out.println("res = "+ res );
		if(res == 1) return "redirect:/msg/memberJoinOk"; 
		else return "redirect:/msg/memberJoinNo";
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberIdCheck", method=RequestMethod.POST)
	public String memberIdCheckPost(String mid) {
		String res ="0";
		MemberVO vo = memberService.getMemberIdCheck(mid);
		
		if(vo != null) res = "1";
		 
		return res;
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberNickCheck", method=RequestMethod.POST)
	public String memberNickCheckPost(String nickName) {
		String res ="0";
		MemberVO vo = memberService.getMemberNickCheck(nickName);
		
		if(vo != null) res = "1";
		
		return res;
	}
	
	// 맴버리스트 ( 원래하던거)
	/*
	@RequestMapping(value= "/memberList", method = RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="pag", defaultValue="1",required = false) int pag,
			@RequestParam(name="pageSize", defaultValue ="3", required = false) int pageSize
			) {
		  // 1. 페이지(pag)를 결정한다.(위에서했다@RequestParam로)
		// 2. 한페이지의 분량을 결정한다.
		// 3. 총 레코드 건수를 구한다.
		int totRecCnt = memberService.totRecCnt();
		// 4. 총 페이지 건수를 구한다.
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) +1;
		// 5. 현재페이지의 시작 인덱스번호를 구한다.
		int startIndexNo = (pag - 1) * pageSize;
		// 6. 현재 화면에 보여주는 시작번호를 구한다.
		int curScrStartNo = totRecCnt - startIndexNo;
		// 블록페이지처리 (3단계) -> 블록의 시작번호를 0번부터 처리했다.
		
		// 1. 블록의 크기를 결정한다. (여기선 3으로 지정)
		int blockSize = 3;
		
		// 2. 현재페이지가 위치하고 있는 블록 번호를 구한다. (예:1페이지는 0블록, 3페이지는 0블록, 5페이지는 1블록)
//		int curBlock = (pag % blockSize)==0? (pag / blockSize) - 1 : (pag / blockSize) ; 
		int curBlock = (pag -1)/blockSize; 
		
		//3. 마지막블록을 구한다.
//		int lastBlock = (totPage % blockSize)==0 ? (totPage / blockSize) -1 : (totPage / blockSize);
		int lastBlock = (totPage -1) / blockSize;
		//		ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<GuestVO> vos = memberService.getGuestList(startIndexNo,pageSize);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);

		
		return "member/memberList";
	}
	*/
	
	//전체 리스트와 검색리스트를 하나의 메소드로 처리(동적처리)
	/*
	@RequestMapping(value= "/memberList", method = RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue="",required = false) String mid,
			@RequestParam(name="pag", defaultValue="1",required = false) int pag,
			@RequestParam(name="pageSize", defaultValue ="3", required = false) int pageSize
			) {
		// 1. 페이지(pag)를 결정한다.(위에서했다@RequestParam로)
		// 2. 한페이지의 분량을 결정한다.
		// 3. 총 레코드 건수를 구한다.
		int totRecCnt = memberService.totTermRecCnt(mid);
		// 4. 총 페이지 건수를 구한다.
		int totPage = (totRecCnt % pageSize)==0 ? totRecCnt / pageSize : (totRecCnt / pageSize) +1;
		// 5. 현재페이지의 시작 인덱스번호를 구한다.
		int startIndexNo = (pag - 1) * pageSize;
		// 6. 현재 화면에 보여주는 시작번호를 구한다.
		int curScrStartNo = totRecCnt - startIndexNo;
		// 블록페이지처리 (3단계) -> 블록의 시작번호를 0번부터 처리했다.
		
		// 1. 블록의 크기를 결정한다. (여기선 3으로 지정)
		int blockSize = 3;
		
		// 2. 현재페이지가 위치하고 있는 블록 번호를 구한다. (예:1페이지는 0블록, 3페이지는 0블록, 5페이지는 1블록)
//		int curBlock = (pag % blockSize)==0? (pag / blockSize) - 1 : (pag / blockSize) ; 
		int curBlock = (pag -1)/blockSize; 
		
		//3. 마지막블록을 구한다.
//		int lastBlock = (totPage % blockSize)==0 ? (totPage / blockSize) -1 : (totPage / blockSize);
		int lastBlock = (totPage -1) / blockSize;
		//		ArrayList<GuestVO> vos = dao.getGuestList();
		ArrayList<GuestVO> vos = memberService.getTermGuestList(startIndexNo,pageSize,mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pag", pag);
		model.addAttribute("totRecCnt", totRecCnt);
		model.addAttribute("totPage", totPage);
		model.addAttribute("curScrStartNo", curScrStartNo);
		model.addAttribute("blockSize", blockSize);
		model.addAttribute("curBlock", curBlock);
		model.addAttribute("lastBlock", lastBlock);
		
		model.addAttribute("mid",mid);
		
		return "member/memberList";
	}
	*/
	// Pagination 이용하기....
	@RequestMapping(value= "/memberList", method = RequestMethod.GET)
	public String memberListGet(Model model,
			@RequestParam(name="mid", defaultValue="",required = false) String mid,
			@RequestParam(name="pag", defaultValue="1",required = false) int pag,
			@RequestParam(name="pageSize", defaultValue ="3", required = false) int pageSize
			) {
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "member", "","");
		
		List<MemberVO> vos = memberService.getMemberList(pageVO.getStartIndexNo(), pageSize);
		
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
		
		return "member/memberList";
	}
	// 비밀번호 찾기 폼
	@RequestMapping(value = "/memberPwdSearch", method=RequestMethod.GET)
	public String memberPwdSearchGet() {
		return "member/memberPwdSearch";
	}
	// 임시 비밀번호 전송하기
	@ResponseBody
	@RequestMapping(value = "/memberPwdSearch", method=RequestMethod.POST)
	public int memberPwdSearchPost(String mid, String email) {
		
		int res = memberService.memberMidEmailcheck(mid,email);
		if(res != 0 ) {
			memberService.imsiPwd(email,mid);
		}
		System.out.println("res = " + res);
		return res;
	}
	
}
