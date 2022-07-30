package com.spring.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dto.UserDTO;
import com.spring.dto.WorkspaceDTO;
import com.spring.dto.WorkspaceUserDTO;
import com.spring.service.WorkspaceServiceImpl;
import com.spring.service.WorkspaceUserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class WorkspaceUserController {
	private final WorkspaceUserServiceImpl workspaceUserService;
	private final WorkspaceServiceImpl workspaceService;
	
	@GetMapping(value = "/workspace/user/{userNo}")
	public List<WorkspaceUserDTO> findWorkspaceByUser(@PathVariable Long userNo){
		return workspaceUserService.getAllWorkspaceUserByUser(userNo);
	}
	
	//해야함
	@PostMapping(value = "/workspace/user/{workspaceNo}" , consumes = MediaType.APPLICATION_JSON_VALUE)
	public void addWorkspaceUser(@RequestBody List<UserDTO> userDTOs,@PathVariable Long workspaceNo){
		WorkspaceDTO workspaceDTO = workspaceService.getWorkspaceByWorkspaceNo(workspaceNo);
		workspaceUserService.insertAllWorkspaceUserService(userDTOs,workspaceDTO);
	}
}
