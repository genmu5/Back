package com.example.BEF.Course.DTO;

import com.example.BEF.tts.dto.response.Choice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatGPTRes {
    @JsonProperty("choices")
    private List<Choice> choice;
}
