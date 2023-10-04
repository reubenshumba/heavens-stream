package com.heavens.stream.request;

import com.heavens.stream.dtos.MyUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest extends MyUserDto {
    private String password;
}
