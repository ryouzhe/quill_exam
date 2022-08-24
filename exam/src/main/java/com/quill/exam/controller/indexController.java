package com.quill.exam.controller;

import com.quill.exam.domain.Context;
import com.quill.exam.domain.Image;
import com.quill.exam.dto.ContextDto;
import com.quill.exam.repository.ContextRepository;
import com.quill.exam.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class indexController {

    private final ContextRepository contextRepository;
    private final ImageRepository imageRepository;

    // index page, 작성된 글을 list로 출력
    @GetMapping("/")
    public String index(Model model) {
        List<Context> contextList = contextRepository.findAll();
        model.addAttribute("contextList", contextList);
        return "index";
    }

    // write page
    @GetMapping("/write")
    public String write(Model model) {
        Context contextEntity = new Context();
        contextRepository.save(contextEntity);
        model.addAttribute("context", contextEntity);
        return "write";
    }

    // detail page
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Context detailContext = contextRepository.findById(id).get();
        model.addAttribute("context", detailContext);
        return "detail";
    }

    // update page
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        Context updateContext = contextRepository.findById(id).get();
        model.addAttribute("updateContext", updateContext);
        return "update";
    }

    // write page 에서 저장하기 누르면 작성한 content 저장함
    @PostMapping("/content/{id}/save")
    public String contentSave(ContextDto contextDto, @PathVariable Long id) {
        Context contextEntity = contextRepository.findById(id).get();
        contextEntity.setTitle(contextDto.getTitle());
        contextEntity.setContent(contextDto.getContent());

        contextRepository.save(contextEntity);
        return "redirect:/" + contextEntity.getId() + "/tag";
    }

    // Image Tag page
    @GetMapping("/{id}/tag")
    public String imageTag(@PathVariable Long id, Model model) {
        Context contextEntity = contextRepository.findById(id).get();
        List<Image> imageList = imageRepository.findByContext(contextEntity);
        model.addAttribute("imageList", imageList);
        return "imageTag";
    }

    // Image hasTag Value 변경
    @GetMapping("/changeTagValue/{id}")
    @ResponseBody
    public Boolean changeTagValue(@PathVariable Long id) {
        Image imageEntity = imageRepository.findById(id).get();
        imageEntity.setHasTag(!imageEntity.isHasTag());
        imageRepository.save(imageEntity);

        return imageEntity.isHasTag();
    }

    // update page 에서 수정하기 누르면 작성한 content 저장함
    @PostMapping("content/update/{id}")
    public String contentUpdate(ContextDto contextDto, @PathVariable Long id) {
        Context contextEntity = contextRepository.findById(id).get();
        contextEntity.setTitle(contextDto.getTitle());
        contextEntity.setContent(contextDto.getContent());

        contextRepository.save(contextEntity);
        return "redirect:/";
    }

    // Quill Editor 이미지 추가 시 Local Storage에 이미지 저장
    @ResponseBody
    @PostMapping("/imageUpload/{id}")
    public String imgUpload (MultipartFile[] uploadFile, @PathVariable Long id) {

        Context contextEntity = contextRepository.findById(id).get();
        Image imageEntity = new Image();

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
                imageEntity.setFileName(uploadFileName);
                imageEntity.setContext(contextEntity);
                imageRepository.save(imageEntity);
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

    // Local Storage에 저장된 이미지를 Preview로 출력하기
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
