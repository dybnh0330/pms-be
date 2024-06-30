package com.binhnd.pmsbe.services.result;

import com.binhnd.pmsbe.dto.response.ResultResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResultService {

    void uploadFile(MultipartFile[] files, Long recordId);

    Resource loadFileAsResource(String fileName);

    void deleteFile(Long id);

    List<ResultResponse> findAllResultInRecord(Long recordId);

    ResultResponse findResultById(Long id);

}
