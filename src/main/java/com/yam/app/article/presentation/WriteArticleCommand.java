package com.yam.app.article.presentation;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public final class WriteArticleCommand {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String image;
    @Size(max = 3)
    private List<String> tags;
}
