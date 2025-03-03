package com.ocean.whale.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreatePostRequest {
    String postContent;
    String postSubject;
    List<MultipartFile> images;
}
