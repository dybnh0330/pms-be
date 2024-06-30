package com.binhnd.pmsbe.mapper;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.constants.RequestAction;
import com.binhnd.pmsbe.dto.response.ResultResponse;
import com.binhnd.pmsbe.entity.Result;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

public class ResultMapper {

    private ResultMapper() {
    }

    public static ResultResponse toDto(Result result) {

        ResultResponse dto = new ResultResponse();
        dto.setId(result.getId());
        dto.setFileName(result.getFileName());
        dto.setFileType(result.getFileType());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PMSConstants.PREFIX_URL + RequestAction.RESULT + "/downloadFile/")
                .path(result.getFileName())
                .toUriString();

        dto.setFileDownloadUri(fileDownloadUri);
        dto.setCreateTime(result.getCreateTime());
        dto.setCreateBy(result.getCreateBy());

        return dto;
    }

    public static List<ResultResponse> mapAll(List<Result> results) {
        List<ResultResponse> responses = new ArrayList<>();
        for (Result result : results) {
            responses.add(toDto(result));
        }
        return responses;
    }

}
