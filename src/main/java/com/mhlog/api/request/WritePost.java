package com.mhlog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
public class WritePost {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public WritePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public WritePost() {
    }
}
