package com.ape.service;

import com.ape.dto.ImageFileDTO;
import com.ape.exception.ImageFileException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.model.ImageData;
import com.ape.model.ImageFile;
import com.ape.repository.ImageFileRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public Set<String> saveImage(MultipartFile[] file) {
        ImageFile imageFile = null;
        Set<ImageFile> images = new HashSet<>();

        for (MultipartFile each:file){
            String filename = null;

            try {
                imageFile = new ImageFile();
                filename = StringUtils.cleanPath(Objects.requireNonNull(each.getOriginalFilename()));
                ImageData imageData = new ImageData(each.getBytes());
                imageFile.setName(filename);
                imageFile.setType(each.getContentType());
                imageFile.setImageData(imageData);
                imageFile.setShowcase(false);
            } catch (IOException e) {
                throw new ImageFileException(e.getMessage());
            }
            imageFileRepository.save(imageFile);
            images.add(imageFile);
        }
        return images.stream().map(ImageFile::getId).collect(Collectors.toSet());
    }

    public List<ImageFileDTO> getAllImages(){
        List<ImageFile> imageFileList = imageFileRepository.findAll();
        return imageFileList.stream().map(imageFile -> {String imageUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/download")
                .path(imageFile.getId())
                .toUriString();
            return new ImageFileDTO(imageFile.getName(),imageUri,imageFile.getType());
        } ).collect(Collectors.toList());
    }

    public ImageFile getImageById(String id){
        return imageFileRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE,id)));
    }

    public void removeById(String id) {
        ImageFile imageFile =  getImageById(id);
        imageFileRepository.delete(imageFile);
    }

    public void setShowcaseImage(Long productId,String imageId) {
//        Product product = productRepository.findProductById(productId).orElseThrow(()->
//                new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND_MESSAGE));
//        ImageFile imageFile = getImageById(imageId);
//        for (ImageFile each:product.getImage()) {
//            each.setShowcase(false);
//        }
//        imageFile.setShowcase(true);
//        imageFileRepository.save(imageFile);
    }

}
