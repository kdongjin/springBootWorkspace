package com.hi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hi.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
}
