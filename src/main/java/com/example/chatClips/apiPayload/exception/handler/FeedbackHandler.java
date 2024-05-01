package com.example.chatClips.apiPayload.exception.handler;

import com.example.chatClips.apiPayload.code.BaseErrorCode;
import com.example.chatClips.apiPayload.exception.GeneralException;

public class FeedbackHandler extends GeneralException {

    public FeedbackHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}