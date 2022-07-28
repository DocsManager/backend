package com.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

	private Long noticeNo;
	
	private Long sender;
	
	private Long receiver;
	
	private String content;
}