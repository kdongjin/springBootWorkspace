package com.zeus.controller;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zeus.dto.BoardDTO;
import com.zeus.exception.BoardRecordNotFoundException;
import com.zeus.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@MapperScan(basePackages = "com.zeus.mapper")
public class BoardController {

	// @Autowired
	private BoardService boardService;

	@Autowired
	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

//	@Autowired
//	public BoardController(BoardService boardService) {
//		this.boardService = boardService;
//	}

	// 게시판 입력창화면 요청
	@GetMapping(value = "/board/insertForm")
	public String boardInsertForm(BoardDTO boardDTO, Model model) {
		model.addAttribute("boardDTO", boardDTO);
		return "board/insertForm";
	}

	// 게시판 입력저장 요청
	@PostMapping(value = "/board/insert")
	public String boardInsert(@Validated BoardDTO boardDTO, BindingResult bindResult, Model model, RedirectAttributes rttr) throws Exception {
		log.info("board/insert" + boardDTO.toString());
		
		if(bindResult.hasErrors()) {
			return "board/insertForm";
		}
		
		
		
		boolean result = boardService.insert(boardDTO);

		if (result == false) {
			//rttr.addFlashAttribute("msg", "게시글입력이 실패되었습니다.");
			throw new BoardRecordNotFoundException("title이 입력이 안되었습니다."+boardDTO.toString()); 
		} else {
			rttr.addFlashAttribute("msg", "게시글입력이 성공되었습니다.");
			rttr.addAttribute("writer", boardDTO.getWriter()); // /board/list?writer=kdj
		}
		return "redirect:/board/list";
	}

	// 게시판 리스트 요청
	@GetMapping(value = "/board/list")
	public String boardList(Model model) throws Exception {

		List<BoardDTO> list = boardService.list();
		if (list == null || list.size() <= 0) {
			return "board/fail";
		}
		model.addAttribute("list", list);

		return "board/list";
	}

	// 선택된 게시글 요청
	@GetMapping(value = "/board/select")
	public String boardSelect(BoardDTO boardDTO, Model model) throws Exception {
		if (boardDTO.getBoardNo() <= 0) {
			return "board/fail";
		}
		boardDTO = boardService.select(boardDTO);
		if (boardDTO == null) {
			//return "board/fail";
			throw new BoardRecordNotFoundException(boardDTO.getBoardNo()+"번 게시글은 없는 게시글입니다.!");
		}
		model.addAttribute("boardDTO", boardDTO);
		return "board/select";
	}

	// 선택된 게시글 삭제요청
	@GetMapping(value = "/board/delete")
	public String boardDelete(BoardDTO boardDTO, Model model) throws Exception {
		if (boardDTO.getBoardNo() <= 0) {
			return "board/fail";
		}
		boolean result = boardService.delete(boardDTO);
		if (result == false) {
			return "board/fail";
		}

		return "board/success";
	}

	// 게시판 수정폼 화면 요청
	@GetMapping(value = "/board/updateForm")
	public String boardUpdateForm(BoardDTO boardDTO, Model model) throws Exception {
		if (boardDTO.getBoardNo() <= 0) {
			return "board/fail";
		}
		boardDTO = boardService.select(boardDTO);

		model.addAttribute("boardDTO", boardDTO);
		return "board/updateForm";
	}

	// 게시판내용 수정 요청
	@PostMapping(value = "/board/update")
	public String boardUpdate(BoardDTO boardDTO, Model model) throws Exception {
		if (boardDTO.getBoardNo() <= 0) {
			return "board/fail";
		}
		boolean result = boardService.update(boardDTO);

		if (result == false) {
			return "board/fail";
		}

		return "board/success";
	}

	// 게시판 수정폼 화면 요청
	@PostMapping(value = "/board/search")
	public String boardSearch(BoardDTO boardDTO, Model model) throws Exception {
		List<BoardDTO> list = boardService.search(boardDTO);
		if (list == null || list.size() <= 0) {
			return "board/fail";
		}
		model.addAttribute("list", list);
		return "board/list"; 
	}
}









