package com.ape.controller;

import com.ape.dto.ImageFileDTO;
import com.ape.model.ImageFile;
import com.ape.service.ImageFileService;
import com.ape.utility.APEResponse;
import com.ape.utility.ImageSavedResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageFileController {

    private final ImageFileService imageFileService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("image") MultipartFile[] image) {
        Set<String> images = imageFileService.saveImage(image);
        ImageSavedResponse response = new ImageSavedResponse(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true, images);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/showcase")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse> setShowcaseImage(@RequestParam("productId") Long productId,
                                                        @RequestParam("imageId") String imageId){
        imageFileService.setShowcaseImage(productId,imageId);
        APEResponse response = new APEResponse(ResponseMessage.IMAGE_SHOWCASE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable String id){
        ImageFile imageFile = imageFileService.getImageById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="+imageFile.getName()).body(imageFile.getImageData().getData());
    }

    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayImage(@PathVariable String id){
        ImageFile imageFile = imageFileService.getImageById(id);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageFile.getImageData().getData(), header, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages(){
        List<ImageFileDTO> imageList=imageFileService.getAllImages();
        return ResponseEntity.ok(imageList);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APEResponse>deleteImageFile(@PathVariable String id){
        imageFileService.removeById(id);
        APEResponse response =new APEResponse(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
