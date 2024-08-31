package com.management.studentstays.App.impl;

import com.management.studentstays.App.service.FileService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

  @Override
  public String uploadImage(String path, MultipartFile file) throws IOException {
    String name = file.getOriginalFilename();

    // random name generate file
    String randomID = UUID.randomUUID().toString();
    String fileName = randomID.concat(name.substring(name.lastIndexOf(".")));

    // Fullpath
    String filepath = path + File.separator + fileName;

    // create folder if not created
    File f = new File(path);
    if (!f.exists()) {
      f.mkdir();
    }

    // File copy
    Files.copy(file.getInputStream(), Paths.get(filepath));
    return fileName;
  }

  @Override
  public InputStream getResource(String path, String fileName) throws FileNotFoundException {
    String fullPath = path + File.separator + fileName;
    InputStream inStream = new FileInputStream(fullPath);
    return inStream;
  }
}
