package com.zeus.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zeus.domain.Item;
import com.zeus.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@MapperScan(basePackages = "com.zeus.mapper")
public class ItemController {

	@Autowired
	private ItemService service;

	@Value("${upload.path}")
	private String uploadPath;

	// File upload 화면 요청
	@RequestMapping(value = "/item/insertForm", method = RequestMethod.GET)
	public String itemInsertForm(Item item, Model model) throws Exception {

		return "item/insertForm";
	}

	// File upload 데이타베이스 저장요청
	// File 을 어떻게 저장할것인가?
	@RequestMapping(value = "/item/insert", method = RequestMethod.POST)
	public String itemInsert(Item item, Model model) throws Exception {
		// 1. fileupload ,파일을 가져온다.
		MultipartFile file = item.getPicture();
		
		// 2. upload한 파일정보를 로그에 출력한다.
		log.info("원래파일명: " + file.getOriginalFilename()); // 홍길동.jpg
		log.info("파일명사이즈: " + file.getSize()); // 10KByte
		log.info("파일타입: " + file.getContentType()); // jpg

		// 파일명에 중복되지 않는 파일명을 만들고, 그 파일에 원본파일에 내용을 복사하고, 중복되지않는 파일명을 리턴받는다.
		String createdFileName = uploadFile(file.getOriginalFilename(), file.getBytes());
		// 7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg
		item.setPictureUrl(createdFileName);
		// item 테이블에 업로드 정보를 저장
		boolean result = service.insert(item);
		
		if (result == false) {
			return "item/fail";
		}
		return "item/success";
	}

	// File upload list 요청
	@RequestMapping(value = "/item/list", method = RequestMethod.GET)
	public String ItemList(Item item, Model model) throws Exception {
		List<Item> list = service.list();

		model.addAttribute("list", list);
		return "item/list";
	}

	@GetMapping("/item/deleteForm")
	public String itemDeleteForm(Item item, Model model) throws Exception {
		item = service.select(item);
		model.addAttribute("item", item);
		return "item/deleteForm";
	}

	@PostMapping("/item/delete")
	public String itemDelete(Item item, Model model) throws Exception {
		// 1.삭제할 외부 저장소에 저장되어있는 테이블에서 가져온다.
		item = service.select(item);
		String createFileName = item.getPictureUrl();
		if (createFileName != null) {
			File file = new File(uploadPath + File.separator + createFileName);
			if (file.exists()) {
				file.delete();
			}
		}
		boolean result = service.delete(item);
		if (result == true) {
			return "item/success";
		} else {
			return "item/fail";
		}
	}
	
	@GetMapping("/item/updateForm")
	public String itemUpdateForm(Item item, Model model) throws Exception {
		item = service.select(item);
		model.addAttribute("item", item);
		return "item/updateForm";
	}
	
	@PostMapping("/item/update")
	public String itemUpdate(Item item, Model model) throws Exception {
		//1. 사용자가 선택한 파일객체가져오고, 기존있는 중복되지않는 이미지파일명을 가져온다.
		MultipartFile file = item.getPicture(); 
		String oldFileName = item.getPictureUrl(); 
		
		//2. 사용자가 새로운 파일을 선택했는지 점검(기존의 파일을 스토리지에서 제거)
		if(file != null && file.getSize() > 0) {
			// 파일명에 중복되지 않는 파일명을 만들고, 그 파일에 원본파일에 내용을 복사하고, 중복되지않는 파일명을 리턴받는다.
			String createdFileName = uploadFile(file.getOriginalFilename(), file.getBytes());
			// 7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg
			item.setPictureUrl(createdFileName);
			// 옛날파일을 삭제
			if (oldFileName != null) {
				File _file = new File(uploadPath + File.separator + oldFileName);
				if (_file.exists()) {
					_file.delete();
				}
			}
		}
		//3. 업데이트 
		boolean result = service.update(item);
		if (result == true) {
			return "item/success";
		} else {
			return "item/fail";
		}
	}

	// 요청한 상품이미지를 @ResponseBody 형식으로 전송요청
	@RequestMapping(value = "/item/display", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> itemDisplay(Item item, Model model) throws Exception {
		// 이미지 파일을 -> byte[] 클라이언트 전송 => File => byte[] => InputStream q변수 선언
		InputStream in = null;
		// ResponseEntity<byte[]> 변수선언
		ResponseEntity<byte[]> entity = null;
		// 외장하드에있는 이미지파일명을 가져온다.
		item = service.select(item);
		String createFileName = item.getPictureUrl();
		// 이미지 확장자 정보가 필요함.(Media type)
		// e332308d-a58a-4ad4-80c0-04ae0cd1f77d_bg4.jpg
		int index = createFileName.lastIndexOf(".");

		try {
			String formatName = createFileName.substring(index + 1); // jpg
			//1.미디어 타입을 결정
			MediaType mediaType = null;
			switch (formatName.toUpperCase()) {
			case "JPG":
				mediaType = MediaType.IMAGE_JPEG;
				break;
			case "GIF":
				mediaType = MediaType.IMAGE_GIF;
				break;
			case "PNG":
				mediaType = MediaType.IMAGE_PNG;
				break;
			default:
				mediaType = null;
				break;
			}
			// 2. HttpHeaders 생성
			HttpHeaders httpHeaders = new HttpHeaders();

			// 3. 외장하드에 있는 파일을 읽어온다.
			// C:/upload/e332308d-a58a-4ad4-80c0-04ae0cd1f77d_bg4.jpg => 1byte[] 변경구조 (InputStream)
			in = new FileInputStream(uploadPath + File.separator + createFileName);

			// mediaType null 이 아니면 httpHeader contentType 등록
			if (mediaType != null) {
				httpHeaders.setContentType(mediaType);
			}
			// 4. ResponseEntity<byte[]> entity 객체생성
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), httpHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return entity;

	}

	// *********************************************************************************
	private String uploadFile(String originalName, byte[] fileData) throws Exception {
		// 중복되지않는 ID생성 7c547e42-3079-4b58-bf91-33fc7d139cfc
		UUID uid = UUID.randomUUID();
		// 7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg
		String createdFileName = uid.toString() + "_" + originalName;
		// uploadPath = "C:/upload/7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg
		File target = new File(uploadPath, createdFileName);
		// fileData => C:/upload/7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg" 복사진행
		FileCopyUtils.copy(fileData, target);
		// 7c547e42-3079-4b58-bf91-33fc7d139cfc_홍길동.jpg 리턴
		return createdFileName;
	}
}










