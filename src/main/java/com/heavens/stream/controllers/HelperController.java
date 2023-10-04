package com.heavens.stream.controllers;

import com.heavens.stream.enums.CodeType;
import com.heavens.stream.helpers.Helper;
import com.heavens.stream.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/api/v1/helper")
@Slf4j
public class HelperController {

    @GetMapping("/generateRandomCode")
    public ResponseEntity<Response<Map<String, Object>>> generateRandomCode(
            @RequestParam(defaultValue = "NUMBERS_ONLY") CodeType type,
            @RequestParam(defaultValue = "4") int length
    ) {
        String s = Helper.generateRandomCode(length, type);
        Map<String,Integer> range =new HashMap<>();
        range.put("min", 4);
        range.put("max", 10);
        range.put("defaultValue", 4);


        Map<String, Object> code = new HashMap<>();
        code.put("code", s);
        code.put("length", length);
        code.put("type", type.name());
        code.put("lengthRange", range);
        code.put("otherType", new ArrayList<>(EnumSet.allOf(CodeType.class)));

        Response<Map<String, Object>> successful = Response.successfulResponse("Successful", code);
        log.info("response : {}", successful);
       return ResponseEntity.ok(successful);
    }

}
