package com.spring.javawspring;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.javawspring.pagination.PageProcess;
import com.spring.javawspring.pagination.PageVO;
import com.spring.javawspring.service.AdminService;
import com.spring.javawspring.service.MemberService;
import com.spring.javawspring.vo.MemberVO;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;

	@Autowired //생성하는 개념 (자주써서 써줌)
	PageProcess pageProcess;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping(value = "/adminMain", method=RequestMethod.GET)
	public String adminMainGet() {
		return "admin/adminMain";
	}
	@RequestMapping(value = "/adminLeft", method=RequestMethod.GET)
	public String adminLeftGet() {
		return "admin/adminLeft";
	}
	@RequestMapping(value = "/adminContent", method=RequestMethod.GET)
	public String adminContentGet() {
		return "admin/adminContent";
	}
	@RequestMapping(value = "/member/adminMemberList", method=RequestMethod.GET)
	public String adminMemberListGet(Model model,
		@RequestParam(name="search", defaultValue = "", required = false) String search,
		@RequestParam(name="part", defaultValue = "", required = false) String part,
		@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
		@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize
		) {
		
		if(part.equals("level")) {
			if(search.matches("준회원|준|준회")) search = "4";
			else if(search.matches("정회원|정|정회")) search = "3";
			else if(search.matches("우수회원|우수|우")) search = "2";
			else if(search.matches("운영자|운|운영")) search = "1";
			else if(search.matches("관리자|관|관리")) search = "0";
		}
		
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "member", part, search);
		List<MemberVO> vos = memberService.getMemberList(pageVO.getStartIndexNo(), pageSize, part, search);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
	
		return "admin/member/adminMemberList";
	}
	/*
	@RequestMapping(value = "/member/adminMemberList", method=RequestMethod.POST)
	public String adminMemberListPost(Model model,
			@RequestParam(name="mid", defaultValue = "", required = false) String mid,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "3", required = false) int pageSize
			) {
		
		PageVO pageVO = pageProcess.totRecCnt(pag, pageSize, "member", "", mid);
		
		List<MemberVO> vos = memberService.getMemberList(pageVO.getStartIndexNo(), pageSize, "", mid);
		
		model.addAttribute("vos", vos);
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("mid", mid);
		
		return "admin/member/adminMemberList";
	}
	*/
	
	// 회원 등급 변경하기
	@ResponseBody
	@RequestMapping(value= "/member/adminMemberLevel", method=RequestMethod.POST)
	public String adminMemberLevelPost(int idx, int level) {
		int res =  adminService.setMemberLevelCheck(idx, level);
		return res+"";
	}
	// 회원 탈퇴 처리하기
	@RequestMapping(value= "/member/adminMemberDel", method=RequestMethod.GET)
	public String adminMemberDelGet(Model model, int idx, 
			@RequestParam(name="search", defaultValue = "", required = false) String search,
			@RequestParam(name="part", defaultValue = "", required = false) String part,
			@RequestParam(name="pag", defaultValue = "1", required = false) int pag,
			@RequestParam(name="pageSize", defaultValue = "5", required = false) int pageSize) {
		
		
		int res =  adminService.MemberDeleteOk(idx);
		model.addAttribute("flag","?pag="+pag+"&pageSize="+pageSize+"&search="+search+"&part="+part);
		if(res==1) {
			return "redirect:/msg/adminMemberDeleteOk";
		}
		else {
			return "redirect:/msg/adminMemberDeleteNo";
		}
	}
	
	
	
	
	// ckeditor 폴더의 파일 리스트 보여주기
	@RequestMapping(value= "/file/fileList", method=RequestMethod.GET)
	public String fileListGet(HttpServletRequest request, Model model) {
		String realPath = request.getRealPath("/resources/data/ckeditor/");
		
		String[] files = new File(realPath).list();
		
		model.addAttribute("files",files);
		
		return "admin/file/fileList";
	}
}

