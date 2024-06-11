package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.SummarizeConverter;
import com.example.chatClips.dto.SummarizeRequestDTO;
import com.example.chatClips.dto.SummarizeResponseDTO;
import com.example.chatClips.service.SummarizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/summarize")
public class SummarizeController {

    private final SummarizeService summarizeService;
    @GetMapping("/get_summary")
    public ApiResponse<SummarizeResponseDTO.GetSummary> summarize(@RequestParam String roomId) {
        return ApiResponse.onSuccess(SummarizeConverter.toSummarize(summarizeService.summary(roomId)));
    }

    @PostMapping("/save")
    public ApiResponse<SummarizeResponseDTO.Save> save(@RequestBody SummarizeRequestDTO.Save request){
        return ApiResponse.onSuccess(SummarizeConverter.toSave(summarizeService.save(request)));
    }
}
