package com.ocean.whale.exception;

public enum WhaleException {
    UNAUTHENTICATED, // cannot verify accessToken
    FIREBASE_ERROR,
    BAD_DATA_ERROR,
    NO_USERNAME_TO_CREATE_USER,
    S3_UPLOAD_ERROR,
}
