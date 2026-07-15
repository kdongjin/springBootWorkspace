package com.zeus.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zeus.domain.Board;
import com.zeus.dto.BoardDTO;
import com.zeus.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper mapper;

	@Override
	@Transactional
	public boolean insert(BoardDTO boardDTO) throws Exception {
		if (boardDTO == null || boardDTO.getTitle().isEmpty() == true) {
			return false;
		}
		Board board = new Board();
		board.setTitle(boardDTO.getTitle());
		board.setWriter(boardDTO.getWriter());
		board.setContent(boardDTO.getContent());

		int count = mapper.insert(board);

		return count == 0 ? false : true;
	}

	@Override
	@Transactional(readOnly = true)
	public BoardDTO select(BoardDTO boardDTO) throws Exception {
		Board board = new Board();
		board.setBoardNo(boardDTO.getBoardNo());

		board = mapper.select(board);

		boardDTO.setBoardNo((board.getBoardNo()));
		boardDTO.setTitle(board.getTitle());
		boardDTO.setContent(board.getContent());
		boardDTO.setRegDate(board.getRegDate());
		boardDTO.setWriter(board.getWriter());

		return boardDTO;
	}

	@Override
	@Transactional
	public boolean update(BoardDTO boardDTO) throws Exception {
		// 1. DB에서 게시글 조회 (값이 없으면 NoSuchElementException 발생)
		Board board = new Board();
		board.setBoardNo(boardDTO.getBoardNo());
		board.setTitle(boardDTO.getTitle());
		board.setWriter(boardDTO.getWriter());
		board.setContent(boardDTO.getContent());

		int count = mapper.update(board);

		return count == 0 ? false : true;
	}

	@Override
	@Transactional
	public boolean delete(BoardDTO boardDTO) throws Exception {

		Board board = new Board();
		board.setBoardNo(boardDTO.getBoardNo());
		int count = mapper.delete(board);

		return count == 0 ? false : true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BoardDTO> list() throws Exception {
		// select * from board order by board_no desc;
		List<Board> list = mapper.list();

		if (list.size() <= 0) {
			return null;
		}
		List<BoardDTO> list2 = new ArrayList<>();

		for (Board board : list) {
			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setContent(board.getContent());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setTitle(board.getTitle());
			boardDTO.setWriter(board.getWriter());
			list2.add(boardDTO);
		}
		return list2;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BoardDTO> search(BoardDTO boardDTO) throws Exception {
		List<Board> list = mapper.search(boardDTO.getTitle());

		if (list.size() <= 0) {
			return null;
		}
		List<BoardDTO> listDTO = new ArrayList<>();

		for (Board board : list) {
			BoardDTO _boardDTO = new BoardDTO();
			_boardDTO.setBoardNo(board.getBoardNo());
			_boardDTO.setContent(board.getContent());
			_boardDTO.setRegDate(board.getRegDate());
			_boardDTO.setTitle(board.getTitle());
			_boardDTO.setWriter(board.getWriter());
			listDTO.add(_boardDTO);
		}

		return listDTO;
	}

}
