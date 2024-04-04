package com.example.chatClips.apiPayload.exception.handler;

import com.example.chatClips.apiPayload.code.BaseErrorCode;
import com.example.chatClips.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {

    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}

