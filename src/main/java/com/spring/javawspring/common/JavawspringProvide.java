package com.spring.javawspring.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

public class JavawspringProvide {
	public int fileUpload(MultipartFile fName) {
		int res = 0;
		try {
			UUID uid = UUID.randomUUID();  // 파일명이 중복되지않게 설정하기위해 써준다.
			String oFileName = fName.getOriginalFilename();
			String saveFileName = uid + "_" + oFileName;
			
			writeFile(fName,saveFileName, "");
			res = 1;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return res;
	}

	public void writeFile(MultipartFile fName, String saveFileName, String flag) throws IOException {
		byte[] data = fName.getBytes();
		
		// request 객체를 쓰기위해서 이렇게 해준다.
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String realPath = request.getSession().getServletContext().getRealPath("resources/"+flag+"/");
		
		FileOutputStream fos = new FileOutputStream(realPath + saveFileName);
		fos.write(data); // 저장하는 명령어
		fos.close();  //FileOutputStream를 닫아줘야한다.
	}
}
