package com.mhlog.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mhlog.api.exception.InvalidRequest;
import lombok.*;

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
    public WritePost(@JsonProperty("title") String title, @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
    }

    public WritePost() {
    }

    public void validate() {

        // 단어 필터
        if(title.contains("욕설")) {
            throw new InvalidRequest();
        }
    }
}
