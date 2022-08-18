package com.quill.exam.controller;

import com.quill.exam.domain.Context;
import com.quill.exam.dto.ContextDto;
import com.quill.exam.repository.ContextRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class indexController {

    private final ContextRepository contextRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/content/save")
    public String contentSave(ContextDto contextDto) {
        Context contextEntity = new Context();
        contextEntity.setTitle(contextDto.getTitle());
        contextEntity.setContent(contextDto.getContent());

        contextRepository.save(contextEntity);

        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/imageUpload")
    public String imgUpload (MultipartFile[] uploadFile) {
        // 업로드 파일 저장 경로

        String uploadFolder = "C:\\Temp";
        String result;

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdt.format(date);

        String datePath = formatDate.replace("-", File.separator);

        File uploadPath = new File(uploadFolder, datePath);

        if(uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for(MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();

            String uuid = UUID.randomUUID().toString();
            uploadFileName = uuid + "_" + uploadFileName;

            File saveFile = new File(uploadFolder, uploadFileName);
            result = uploadFolder + "\\" + uploadFileName;

            try {
                multipartFile.transferTo(saveFile);
                System.out.println("-------------------");
                System.out.println("Image save complete");
                System.out.println("-------------------");
                return result;
            } catch (Exception e) {
                System.out.println("-------------------");
                e.printStackTrace();
                System.out.println("-------------------");
            }
        }
        System.out.println("Image save failed");
        return null;
    }

    @ResponseBody
    @GetMapping("/display")
    public ResponseEntity<byte[]> showImageGET(@RequestParam("filePath") String filePath) {
        System.out.println("-----------------------");
        System.out.println("Controller showImageGet");
        System.out.println("fileName: " + filePath);
        System.out.println("-----------------------");

        File file = new File(filePath);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
