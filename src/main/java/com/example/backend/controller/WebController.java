
package com.example.backend.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


@RestController
public class WebController {

    @GetMapping("/")
    public ResponseEntity<InputStreamResource> download() throws Exception {

        int number_of_lines = 2000000;
        File temp = new File("textfile.csv");
        temp.delete();
        FileOutputStream outputStream = new FileOutputStream(temp);


        for (int i = 0; i < number_of_lines; i++) {
            if(i%100000 == 0)
                System.out.println("Progress: " + (i*100d)/number_of_lines);
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < 80; k++) {
                sb.append(Math.random() + ",");
            }
            byte[] buffer = (sb.toString() + "\n").getBytes();
            outputStream.write(buffer);
        }
        outputStream.close();


        HttpHeaders respHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "csv");
        respHeaders.setContentType(mediaType);
        respHeaders.setContentDispositionFormData("attachment", "xyz.csv");
        File file = new File("textfile.csv");
        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
        return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);


    }

}

