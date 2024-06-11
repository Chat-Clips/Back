package com.example.chatClips.converter;

import com.example.chatClips.domain.Summarize;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.SummarizeResponseDTO;
import com.example.chatClips.dto.UserResponseDTO;

public class SummarizeConverter {

    public static SummarizeResponseDTO.GetSummary toSummarize(Summarize summarize){
        return SummarizeResponseDTO.GetSummary.builder()
            .summarize(summarize.getSummary())
            .build();
    }
    public static SummarizeResponseDTO.Save toSave(Summarize summarize){
        return SummarizeResponseDTO.Save.builder()
            .id(summarize.getId())
            .build();
    }
}
