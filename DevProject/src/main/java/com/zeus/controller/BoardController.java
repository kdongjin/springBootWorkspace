package com.zeus.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zeus.dto.BoardDTO;

import lombok.extern.java.Log;

//뷰화면에서 사용자 요청을 받는 클래스
@Log
@Controller
@RequestMapping(value="/board")
public class BoardController {
	
	@RequestMapping(value = "/ajaxhome", method = RequestMethod.GET )
	public String ajaxhome() {
		return "board/ajaxhome"; 
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET )
	public String home() {
		return "board/home"; 
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert() {
		return "board/insert"; 
	}

	@RequestMapping(value = "/select/{boardNo}" , method = RequestMethod.GET)
	public String select1(@PathVariable("boardNo") int boardNo, Model model) {
		model.addAttribute("boardNo", boardNo);
		return "board/select"; 
	}
	
	@RequestMapping(value = "/select" , method = RequestMethod.GET)
	public String select2(Model model) {
		model.addAttribute("boardNo", "해당된 번호는 없음.");
		return "board/select"; 
	}
	
	@GetMapping(value = "/select" , params = "register")
	public String select3(Model model) {
		model.addAttribute("boardNo", "reister=값없음");
		return "board/select"; 
	}
	
	@RequestMapping(value = "/delete" , method = RequestMethod.GET)
	public String delete(Model model) {
		return "board/delete"; 
	}
	
	@RequestMapping(value = "/update" , method = RequestMethod.GET)
	public String update1(Model model) {
		return "board/update"; 
	}

	@PutMapping(value = "/update/{boardNo}", consumes = "application/json")
	public ResponseEntity<String> update2(@PathVariable("boardNo") int boardNo, @RequestBody BoardDTO boardDTO) {
		log.info("boardDTO ="+boardDTO);
		ResponseEntity<String> message = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		return message; 
	}
	
	@PutMapping(value = "/update/{boardNo}", headers = "X-HTTP-Method-Override=PUT")
	public ResponseEntity<String> update3(@PathVariable("boardNo") int boardNo, @RequestBody BoardDTO boardDTO) {
		log.info("headers  boardDTO ="+boardDTO);
		ResponseEntity<String> message = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		return message; 
	}
}











