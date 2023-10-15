package com.madhu.service;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import com.madhu.exception.EncryptionException;

public interface EncryptionService {

	String encryptText(String plainText, String secretKey) throws EncryptionException, Exception;

	String decryptText(String encryptedText, String secretKey) throws EncryptionException, Exception;

	FileSystemResource encryptFile(MultipartFile plainFile, String secretKey) throws EncryptionException, Exception;

	String decryptFile(MultipartFile encryptedFile, String secretKey,String extension) throws EncryptionException, Exception;


}
