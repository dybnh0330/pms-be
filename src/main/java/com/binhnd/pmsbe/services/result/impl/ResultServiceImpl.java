package com.binhnd.pmsbe.services.result.impl;

import com.binhnd.pmsbe.common.constants.PMSConstants;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.common.utils.jwt.SecurityUtils;
import com.binhnd.pmsbe.dto.FileStorageProperties;
import com.binhnd.pmsbe.dto.response.ResultResponse;
import com.binhnd.pmsbe.entity.MedicalRecord;
import com.binhnd.pmsbe.entity.Result;
import com.binhnd.pmsbe.mapper.ResultMapper;
import com.binhnd.pmsbe.repositories.MedicalRecordRepository;
import com.binhnd.pmsbe.repositories.ResultRepository;
import com.binhnd.pmsbe.services.result.ResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ResultServiceImpl implements ResultService {

    private final Path fileStorageLocation;
    private final ResultRepository resultRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ServletContext servletContext;

    private final Logger LOGGER = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    public ResultServiceImpl(FileStorageProperties fileStorageProperties,
                             ServletContext servletContext,
                             ResultRepository resultRepository,
                             MedicalRecordRepository medicalRecordRepository) {
        this.fileStorageLocation = Paths
                .get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        this.servletContext = servletContext;
        this.resultRepository = resultRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        init();
    }

    @Override
    public void uploadFile(MultipartFile[] files, Long recordId) {

        if (!ObjectUtils.isEmpty(files)) {

            for (MultipartFile file : files) {

                LOGGER.error(file.getContentType());

                String mimeType = servletContext.getMimeType(file.getOriginalFilename());
                LOGGER.error(mimeType);
            }


            SecurityUtils.allowFilesTypeOrThrow(servletContext, PMSConstants.ALLOWED_FILE_TYPE, files);
        }

        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(recordId);

        if (medicalRecord.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        if (!Files.exists(fileStorageLocation)) {
            init();
        }

        for (MultipartFile file : files) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            String fileName = LocalDateTime.now().format(formatter) + "_"
                    + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()))
                    .replace(" ", "_");

            try {

                Result result = new Result();

                if (fileName.contains("..")) {
                    throw new PMSException(EnumPMSException.FILE_NAME_ERROR);
                }

                Path targetLocation = this.fileStorageLocation.resolve(fileName);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String path = targetLocation.toString().replace("\\", "/");

                result.setFileName(fileName);
                result.setFileUrl(path);
                result.setFileType(file.getContentType());
                result.setCreateTime(Timestamp.from(Instant.now()));
                result.setCreateBy(SecurityUtils.getCurrentUsername());
                result.setMedicalRecord(medicalRecord.get());

                resultRepository.save(result);

            } catch (IOException e) {
                throw new PMSException(EnumPMSException.FILE_NOT_FOUND);
            }
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new PMSException(EnumPMSException.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            throw new PMSException(EnumPMSException.FILE_NOT_FOUND);
        }
    }

    @Override
    public void deleteFile(Long id) {

        Optional<Result> file = resultRepository.findById(id);

        if (file.isEmpty()) {
            throw new PMSException(EnumPMSException.RESULT_NOT_FOUND);
        }

        Result result = file.get();

        Path path = this.fileStorageLocation.resolve(result.getFileName());

        if (ObjectUtils.isEmpty(path)) {
            throw new PMSException(EnumPMSException.PATH_NOT_EXISTED);
        }

        try {
            boolean isDeleted = Files.deleteIfExists(path);

            if (!isDeleted) {
                throw new PMSException(EnumPMSException.FILE_NOT_FOUND);
            }

            resultRepository.delete(result);

        } catch (Exception e) {
            LOGGER.error("Delete File Exception: ", e);
        }
    }

    @Override
    public List<ResultResponse> findAllResultInRecord(Long recordId) {

        Optional<MedicalRecord> record = medicalRecordRepository.findById(recordId);
        if (record.isEmpty()) {
            throw new PMSException(EnumPMSException.MEDICAL_RECORD_NOT_EXISTED);
        }

        MedicalRecord medicalRecord = record.get();
        List<Result> resultByMedicalRecord = resultRepository.findResultByMedicalRecord(medicalRecord);

        return ResultMapper.mapAll(resultByMedicalRecord);
    }

    @Override
    public ResultResponse findResultById(Long id) {

        Optional<Result> result = resultRepository.findById(id);

        if (result.isEmpty()) {
            throw new PMSException(EnumPMSException.RESULT_NOT_FOUND);
        }

        return ResultMapper.toDto(result.get());
    }

    private void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new PMSException(EnumPMSException.CREATE_DIRECTORY_FAIL);
        }
    }
}
