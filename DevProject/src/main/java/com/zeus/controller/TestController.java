package com.zeus.controller;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.zeus.dto.Address;
import com.zeus.dto.BoardDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TestController {

	@GetMapping("/test/posthome")
	public void posthome() {
	}
	
	@GetMapping("/test/ajaxhome4")
	public void posthome2() {
	}
	
	@RequestMapping(value="/test/gohome1", method = RequestMethod.POST)
//	@ResponseBody 
	public String gohome(Model model, BoardDTO bd) {
		ArrayList<MultipartFile> list = bd.getPicture();
		
		for( MultipartFile p  : list ) {
			log.info("/test/gohome1  = "+p.getOriginalFilename());
			log.info("/test/gohome1  = "+p.getSize());
			log.info("/test/gohome1  = "+p.getContentType());
		}  
		return "test/gohome1";
	}
	
	@RequestMapping(value="/test/gohome2", method = RequestMethod.POST)
	public ResponseEntity<String> gohome2(Model model, @RequestBody ArrayList<BoardDTO> bdList) {
		log.info("/test/gohome2 " );
		for( BoardDTO bd  : bdList ) {
			log.info("/test/gohome1  boardNo= "+bd.getBoardNo());
			log.info("/test/gohome1  = "+bd.getTitle());
		}
		
		ResponseEntity<String> entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		return entity;
	}
	
	@PostMapping(value="/test/gohome3", produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> gohome3(MultipartFile file) {
		log.info("/test/gohome1  = "+file.getOriginalFilename());
		log.info("/test/gohome1  = "+file.getSize());
		log.info("/test/gohome1  = "+file.getContentType());
		
		ResponseEntity<String> entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		return entity;
	}
	
	@RequestMapping(value="/test/gohome1", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<Address> gohomeGet(Model model, @ModelAttribute BoardDTO bdo) {
		ArrayList<Address> addressList = bdo.getAddress();
		log.info("/test/gohome1 userId = "+ addressList.toString());

		return addressList;
	}

}
