package com.madhu.controller;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.madhu.exception.EncryptionException;
import com.madhu.service.EncryptionService;

@Controller
public class EncryptionController {

	@Autowired
	private EncryptionService eService;

	@Value("${file.location}")
	private String uploadLocation;

	@GetMapping(value = { "/", "/home" })
	public String homePage() {
		return "index";
	}

	@PostMapping("/encryptTextProcess")
	public String encryptText(@RequestParam String plainText, @RequestParam(required = false) String secretKey,
			Model model) throws EncryptionException, Exception {

		System.out.println("Inside Encryption of Text with text " + plainText + " and key " + secretKey);

		try {
			String result = eService.encryptText(plainText, secretKey);
			System.out.println("The encrypted text is " + result);

			model.addAttribute("encryptedData", result);
		} catch (Exception e) {
			model.addAttribute("encryptTextError", e.getMessage());
		}

		return "index";

	}

	@PostMapping("/decryptTextProcess")
	public String decryptText(@RequestParam String encryptedText, @RequestParam(required = false) String secretKey,
			Model model) throws EncryptionException, Exception {

		System.out.println("Inside Decryption of Text");

		try {

			String result = eService.decryptText(encryptedText, secretKey);

			model.addAttribute("decryptedData", result);
		} catch (Exception e) {
			model.addAttribute("decryptTextError", e.getMessage());
		}

		return "index";
	}

	@PostMapping("/encryptFileProcess")
	@ResponseBody
	public ResponseEntity<Resource> encryptFile(@RequestParam("file") MultipartFile plainFile,
			@RequestParam(required = false) String secretKey, RedirectAttributes model) throws EncryptionException, Exception {

		System.out.println("Inside Encryption of file");

		try {
			var file = eService.encryptFile(plainFile, secretKey).getFile();

			System.out.println("the file name is " + file.getAbsolutePath());

			System.out.println("file exist ? " + file.exists());

		
				Resource resource = new UrlResource(file.toURI());

				if (resource.exists()) {
					// Set response headers to trigger the download

					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

					// Return the file as a ResponseEntity with appropriate headers
					return ResponseEntity.ok().headers(headers).body(resource);
				} else {
					// Handle the case where the file does not exist
					return ResponseEntity.status(302).header("Location", "/").build();
				}

		} catch (Exception e) {
			System.out.println("The message is "+e.getMessage());
			model.addFlashAttribute("encryptFileError", e.getMessage());
			return ResponseEntity.status(302).header("Location", "/").build();
		}

	}

	@PostMapping("/decryptFileProcess")
	ResponseEntity<Resource> decryptFile(@RequestParam("file") MultipartFile encryptedFile,
			@RequestParam(required = false) String secretKey, RedirectAttributes model) throws EncryptionException, Exception {

		System.out.println("Inside Decryption of file");

		System.out.println("Encrypted File is " + encryptedFile.getOriginalFilename());

		try {

			var file = eService.decryptFile(encryptedFile, secretKey).getFile();

			var filePath = Path.of(file.getPath());

			System.out.println("the file path is " + filePath);

			
				// Load the file as a resource
				Resource resource = new UrlResource(filePath.toUri());

				System.out.println("the resource is " + resource);

				System.out.println(resource.getFilename());

				System.out.println("the resource 2 is " + resource.getFilename());
				if (resource.exists()) {
					// Set response headers to trigger the download
					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

					// Return the file as a ResponseEntity with appropriate headers
					return ResponseEntity.ok().headers(headers).body(resource);
				} else {
					// Handle the case where the file does not exist
					System.out.println("inside not found ");
					return ResponseEntity.status(302).header("Location", "/").build();
				}
		} catch (Exception e) {
			System.out.println("The message is "+e.getMessage());
			model.addFlashAttribute("fileDecryptError", e.getMessage());
			return ResponseEntity.status(302).header("Location", "/").build();
		}

	}

}
