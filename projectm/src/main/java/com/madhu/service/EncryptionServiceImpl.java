package com.madhu.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.madhu.exception.EncryptionException;
import com.madhu.utility.CommonUtils;
import com.madhu.utility.ResourceService;

@Service
public class EncryptionServiceImpl implements EncryptionService {

	@Value("${file.location}")
	private String uploadLocation;

	@Autowired
	private ResourceService rService;

	private String defaultKey = "1234567812345678";

	@Override
	public String encryptText(String plainText, String secretKey) throws Exception {

		return CommonUtils.encrypt(plainText, secretKey);

	}

	@Override
	public String decryptText(String encryptedText, String secretKey) throws Exception {

		return CommonUtils.decrypt(encryptedText, secretKey);
	}

	@Override
	public FileSystemResource encryptFile(MultipartFile plainFile, String secretKey)
			throws EncryptionException, Exception {

		if (secretKey == null)
			secretKey = defaultKey;

		System.out.println("The folder directory is " + uploadLocation);

		File originalFile = new File(getPath() + uploadLocation, plainFile.getOriginalFilename());

		System.out.println("the original file is " + originalFile.getAbsolutePath());

		plainFile.transferTo(originalFile);

		String filename = plainFile.getOriginalFilename();

		int lastIndex = filename.lastIndexOf('.');

		filename = filename.substring(0, lastIndex);

		return utilEncryptFile(filename, secretKey);

	}

	@Override
	public String decryptFile(MultipartFile encryptedFile, String secretKey, String extension) throws Exception {

		if (secretKey == null)
			secretKey = defaultKey;

		System.out.println("the file name is " + encryptedFile.getOriginalFilename());

		String fileName = encryptedFile.getOriginalFilename().replace("__", "/");

		System.out.println("The full file Name is " + fileName);

		int endIndex = fileName.length() - 5;

		fileName = CommonUtils.decrypt(fileName.substring(0, endIndex), secretKey) + "." + extension;

		System.out.println("the updated final plain file name is " + fileName);
		return fileName;

	}

	private FileSystemResource utilEncryptFile(String filename, String secretKey) throws Exception {

		System.out.println("the file name is " + filename);

		filename = CommonUtils.encrypt(filename, secretKey);

		filename = filename.replace("/", "__");

		System.out.println("the encrypted file name is " + filename);

		String path = getPath() + uploadLocation + filename;

		System.out.println("the path is " + path);

		System.out.println("the filename is " + filename);

		File file = new File(filename + ".ENCR");

		file.createNewFile();

		System.out.println("the encrypted file name is " + file.getName());

		return new FileSystemResource(file);

	}

	private String getPath() {

		String path = rService.getResourcesPath();

		path = path.substring(0, path.length() - "target\\classes".length());

		System.out.println("the current path is " + path);

		return path;
	}

}
