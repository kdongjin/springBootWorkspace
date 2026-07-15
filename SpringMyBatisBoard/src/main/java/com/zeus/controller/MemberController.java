package com.zeus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zeus.domain.Member;
import com.zeus.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class MemberController {

	@Autowired
	private MemberService service;

	// 게시판 입력창화면 요청
	@GetMapping(value = "/insertForm")
	public String memberInsertForm(Member member, Model model) {
		model.addAttribute("member", member);
		return "user/insertForm";
	}

	// 게시판 입력저장 요청
	@PostMapping(value = "/insert")
	public String userInsert(Member member, Model model) throws Exception {

		log.info("user/insert" + member.toString());
		boolean result = service.insert(member);
		if (result == true) {
			return "user/success";
		}

		return "user/fail";
	}

	// 사용자 리스트 화면 요청
	@GetMapping(value = "/list")
	public String userList(Member member, Model model) throws Exception {
		List<Member> list = service.list();
		model.addAttribute("list", list);

		return "user/list";
	}

	// 사용자 수정화면 요청
	@GetMapping(value = "/updateForm")
	public String userUpdateForm(Member member, Model model) throws Exception {
		model.addAttribute("member", service.selectMember(member));
		return "user/updateForm";
	}

	// 사용자 수정내용 요청
	@PostMapping(value = "/update")
	public String userUpdate(Member member, Model model) throws Exception {
		boolean result = service.update(member); 
		
		if (result == false) {
			return "user/fail";
		}
		return "user/success";
	}

	// 사용자 삭제 요청
	@GetMapping(value = "/deleteMember")
	public String userDeleteMember(Member member, Model model) throws Exception {
		boolean result = service.deleteMember(member);
		if (result == false) {
			return "user/fail";
		}
		return "user/success";
	}

	// 사용자 권한삭제 요청
	@GetMapping(value = "/deleteAuth")
	@ResponseBody
	public Boolean userDeleteAuth(Member member, Model model) throws Exception {
		member.setUserNo(3);
		return service.deleteAuth(member);
	}

	// 사용자 정보(select) 요청
	@GetMapping(value = "/selectMember")
	public String userSelectMember(Member member, Model model) throws Exception {
		model.addAttribute("member", service.selectMember(member));
		return "user/selectMember";
	}

}
