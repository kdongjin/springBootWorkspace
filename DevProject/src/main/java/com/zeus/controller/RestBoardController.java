package com.zeus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zeus.dto.BoardDTO;

import lombok.extern.java.Log;

//뷰화면에서 사용자 요청을 받는 클래스
@Log
//데이터만 전달받고, 전달하는 리액트 전문 컨트롤로
@RestController
@RequestMapping(value="/board")
public class RestBoardController {
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET )
	public @ResponseBody String hello() {
		return "안녕하세요."; 
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET )
	public @ResponseBody BoardDTO list() {
		BoardDTO bdto = new BoardDTO();
		bdto.setBoardNo(10);
		bdto.setContent("dkfs");
		bdto.setTitle("title");
		bdto.setWriter("kdj");
		
		return bdto; 
	}
	
	@RequestMapping(value = "/listarray", method = RequestMethod.GET )
	public @ResponseBody List<BoardDTO> listarray() {
		List<BoardDTO> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			BoardDTO bdto = new BoardDTO();
			bdto.setBoardNo(i);
			bdto.setContent("dkfs"+i);
			bdto.setTitle("title"+i);
			bdto.setWriter("kdj"+i);
			list.add(bdto);
		}
		
		return list; 
	}
}











