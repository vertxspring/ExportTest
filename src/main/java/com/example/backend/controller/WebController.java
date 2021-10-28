
package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;


@RestController
public class WebController {

    @GetMapping("/")
    public ResponseEntity<InputStreamResource> download() throws Exception {
        Connection conn = DriverManager.getConnection("url");
        conn.setAutoCommit(false);
        Statement st = conn.createStatement();
        st.setFetchSize(10000);
        ResultSet rs = st.executeQuery("SELECT * FROM table");
        File temp = new File("textfile.csv");
        FileOutputStream outputStream = new FileOutputStream(temp);
        while (rs.next()) {
            String s = getCsvRowFromRs(rs);
            byte[] buffer = (s.toString() + "\n").getBytes();
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

    private String getCsvRowFromRs(ResultSet rs) throws SQLException {
        return getCommaSeparatedStrings(rs.getString("column1"), rs.getString("column2"));
    }

    private String getCommaSeparatedStrings(String... s){
        return Arrays.stream(s).collect(Collectors.joining(","));
    }

}

