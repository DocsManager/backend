package com.spring.service;

import com.spring.dto.WorkspaceDTO;

public interface WorkspaceService {
	
	public void insertWorkspace(WorkspaceDTO workspaceDTO);
	
	public void deleteWorkspace(Long workspaceNo);
	
	public WorkspaceDTO getWorkspaceByWorkspaceNo(Long workspaceNo);

	public void updateWorkspace(WorkspaceDTO workspaceDTO);

	
}