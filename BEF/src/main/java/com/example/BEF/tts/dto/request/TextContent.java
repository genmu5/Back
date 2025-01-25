package com.example.BEF.tts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TextContent extends Content {
    private String text;

    public TextContent(String type, String text) {
        super(type);
        this.text = text;
    }
}
