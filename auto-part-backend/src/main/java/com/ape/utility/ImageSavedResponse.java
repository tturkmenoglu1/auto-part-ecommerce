package com.ape.utility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class ImageSavedResponse extends APEResponse{

    private Set<String> imageid;

    public ImageSavedResponse(String message, Boolean success, Set<String> imageid) {
        super(message, success);
        this.imageid = imageid;
    }
}
