package com.binhnd.pmsbe.controller;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.dto.response.ResultResponse;
import com.binhnd.pmsbe.services.result.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.binhnd.pmsbe.common.constants.RequestAction.RESULT;
import static com.binhnd.pmsbe.common.constants.RequestAction.Result.*;

@RestController
@RequestMapping(PMSConstants.PREFIX_URL + RESULT)
@Validated
public class ResultController {

    private final Logger L = LoggerFactory.getLogger(ResultController.class);

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping(value = ADD_RESULT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addResultInRecord(@RequestParam Long id, MultipartFile[] files) {
        L.info("[POST] {}: add result into record", PMSConstants.PREFIX_URL + "/result/add");
        resultService.uploadFile(files, id);
        return ResponseEntity.ok().body("\"Add result into record successfully!\"");
    }

    @DeleteMapping(value = DELETE_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteResultInRecord(@RequestParam Long id) {
        L.info("[DELETE] {}: delete result in record", PMSConstants.PREFIX_URL + "/result/delete?id=" + id);
        resultService.deleteFile(id);
        return ResponseEntity.ok().body("\"Delete result in record successfully!\"");
    }

    @GetMapping(value = FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultResponse>> findAllResultInRecord(@RequestParam Long id) {
        L.info("[GET] {}: find all result in record", PMSConstants.PREFIX_URL + "/result/find-all");
        return ResponseEntity.ok().body(resultService.findAllResultInRecord(id));
    }

    @GetMapping(value = FIND_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> findResultById(@RequestParam Long id) {
        L.info("[GET] {}: find result by id", PMSConstants.PREFIX_URL + "/result/find-by-id?id=" + id);
        return ResponseEntity.ok().body(resultService.findResultById(id));
    }

    @GetMapping( value = DOWNLOAD_RESULT + "/{fileName:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        L.info("[GET] {}: download file result", PMSConstants.PREFIX_URL + "/result/downloadFile/" + fileName);

        Resource resource = resultService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            L.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
