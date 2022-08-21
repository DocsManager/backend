package com.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spring.dto.DocumentDTO;
import com.spring.dto.DocumentUserDTO;
import com.spring.dto.PageRequestDTO;
import com.spring.dto.PageResultDTO;
import com.spring.entity.Document;
import com.spring.entity.User;
import com.spring.entity.WorkspaceUser;
import com.spring.exception.UploadFailedException;
import com.spring.repository.DocumentRepository;
import com.spring.util.S3Util;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
   
   private final DocumentRepository documentRepository;
   private final DocumentUserServiceImpl documentUserService;
    
   @Override
	public PageResultDTO<DocumentDTO, Document> getList(User userNo, PageRequestDTO pageRequestDTO) {
		Pageable pageable = pageRequestDTO.getPageable(Sort.by("documentNo").descending());
		
		Page<Document> result =  documentRepository.findDocumentByUser(userNo, pageable);
		
		// entity -> dto
		Function<Document, DocumentDTO> function = (Document -> Document.toDTO(Document));
		
		return new PageResultDTO<DocumentDTO, Document>(result, function);
	}
   

   // 문서 조회 
   @Override
   public DocumentDTO selectDocument(Long documentNo) {
      Document document = documentRepository.findDocumentByDocumentNo(documentNo);
      return document == null ? null : document.toDTO(document);
   }
   
   // 문서 작성
   
   
   // DB INSERT
   @Override
   @Transactional
   public void insertDocument(DocumentDTO documentDTO ,List<DocumentUserDTO> documentUserList,MultipartFile multipart) {   
         try {
			S3Util.S3Upload(multipart, documentDTO);
			Document document = documentRepository.save(documentDTO.toEntity(documentDTO));
			System.out.println(document);
//			List<DocumentUserDTO> documentUserDTOs = new ArrayList<DocumentUserDTO>();
//			documentDTO.getUserList().forEach(v->documentUserDTOs.add(
//															DocumentUserDTO.builder()
//															.documentNo(document.toDTO(document))
//															.userNo(v)
//															.build()));
			documentUserList.forEach(v->v.setDocumentNo(document.toDTO(document)));
			documentUserService.insertDocumentUser(documentUserList);
		} catch (UploadFailedException e) {
			e.printStackTrace();
		}
         
         
         
      }   
   
   // 문서 수정(파일, 문서 내용)
   @Override
   public void updateDocument(Long documentNo, DocumentDTO documentDTO, MultipartFile multipart) {
      documentDTO.setDocumentNo(documentNo);
      DocumentDTO orginalDTO = selectDocument(documentDTO.getDocumentNo());
      if(orginalDTO != null) {
         S3Util.deleteFile(orginalDTO.getFileName());
         
         try {
			documentDTO = S3Util.S3Upload(multipart, documentDTO);
			DocumentDTO newDTO = new DocumentDTO(orginalDTO, documentDTO);
			documentRepository.save(newDTO.toEntity(newDTO));
		} catch (UploadFailedException e) {
			e.printStackTrace();
		}
      }
   }
   
   // 문서 수정(문서 내용)
   @Override
   public void updateDocument(Long documentNo, DocumentDTO documentDTO) {
      documentDTO.setDocumentNo(documentNo);
      DocumentDTO orginalDTO = selectDocument(documentDTO.getDocumentNo());
      if(orginalDTO != null) {
         DocumentDTO newDTO = new DocumentDTO(orginalDTO, documentDTO);
         documentRepository.save(newDTO.toEntity(newDTO));
      }
   }
   
   // 문서 삭제
   @Override
   public void deleteDocument(List<Long> documentNo) {
	   for (int i = 0; i < documentNo.size(); i++) {
		 S3Util.deleteFile(selectDocument(documentNo.get(i)).getFileName());
		 documentRepository.deleteById(documentNo.get(i));
	}

   }
}