package com.spring.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.MailDTO;
import com.spring.dto.UserDTO;
import com.spring.service.MailService;
import com.spring.service.MailServiceImpl;
import com.spring.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/mail")
@RequiredArgsConstructor
public class MailController {

	BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
	private final MailServiceImpl mailService;
	private final UserServiceImpl userService;

//	@CrossOrigin(origins = { "http://localhost:3000" })
	@GetMapping("/send")
	public String sendMail(@RequestParam(value = "email") String email) throws Exception {
		MailDTO mailDTO = new MailDTO();
		Random random = new Random();
		String code = "";
		for (int i = 0; i < 3; i++) {
			int index = random.nextInt(25) + 65; // A~Z까지 랜덤 알파벳 생성
			code += (char) index;
		}
		int numIndex = random.nextInt(8999) + 1000; // 4자리 정수를 생성
		code += numIndex;
		mailDTO.setAddress(email);
		System.out.println(mailDTO.getAddress());
		mailDTO.setTitle("DM서비스 회원가입을 위한 인증 코드입니다.");
		mailDTO.setCode(code);
		mailDTO.setMessage("인증번호는 " + code + "입니다");
		String newCode = pwEncoder.encode(code);
		System.out.println(newCode);
		
		if(email.isEmpty() && mailDTO.getAddress().isEmpty()) {
		throw new Exception("email address is empty");
		}
		else {
			mailService.sendMail(mailDTO);
		}
		return newCode;
		
	}

//	@CrossOrigin(origins = { "http://localhost:3000" })
	@GetMapping("/verify")
	public boolean verifyMail(@RequestParam(value = "verifyMail") String verifyMail,
			@RequestParam(value = "compareVerify") String compareVerify) {
		System.out.println(verifyMail);
		System.out.println(compareVerify);
		if (pwEncoder.matches(compareVerify, verifyMail)) {
			System.out.println("같음");
			return true;
		} else {
			System.out.println("다름");
			return false;
		}
	}

//	@CrossOrigin(origins = { "http://localhost:3000" })
	@GetMapping("/finduser")
	public List<UserDTO> findUser(@RequestParam(value = "name") String name,
			@RequestParam(value = "email") String email) {
		List<UserDTO> UserDTO = userService.findByNameAndEmail(name, email);
		return UserDTO;
	}

//	@CrossOrigin(origins = { "http://localhost:3000"})
	@GetMapping("/findpassword")
	public @ResponseBody Map<String, Boolean> findPassword(@RequestParam(value = "id") String id,
			@RequestParam(value = "email") String email) {
		Map<String, Boolean> json = new HashMap<>();
		boolean pwFindCheck = userService.userEmailCheck(id, email);
		System.out.println(pwFindCheck);
		json.put("check", pwFindCheck);
		return json;

	}

//	@CrossOrigin(origins = { "http://localhost:3000" })
	@GetMapping("/sendpw")
	public @ResponseBody void sendEmail(String email, String id) {
		MailDTO mailDTO = mailService.createMailAndChangePassword(email, id);
		System.out.println(mailDTO);
		mailService.mailSend(mailDTO);
	}
	
	@GetMapping("/checkmail")
	public @ResponseBody Map<String, Boolean> checkMail(@RequestParam(value = "email") String email) {
		Map<String, Boolean> verifyMail = new HashMap<>();
		
		boolean mailCheck = mailService.mailCheck(email);
		verifyMail.put("check", mailCheck);
		return verifyMail;
	}

	
}
