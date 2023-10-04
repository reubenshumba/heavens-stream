package com.heavens.stream.dtos;

import com.heavens.stream.models.Authority;
import com.heavens.stream.models.MyUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyUserDto {

    private Long id;

    private String username;

    private String email;

    private boolean active;

    public boolean accountNonExpired = true;

    public boolean accountNonLocked = true;

    public boolean credentialsNonExpired = true;

    public boolean delete;

    public boolean edit;

    public boolean create;

    private List<Authority> authorities ;

    private List<HeavenDto> heavensOwned;

    private static final ModelMapper modelMapper = new ModelMapper();


    public static List<MyUserDto> fromMyUsers(List<MyUser> myUsers) {
        return myUsers.stream()
                .map(MyUserDto::fromMyUser)
                .collect(Collectors.toList());
    }

  public static MyUser fromHeavenDto(MyUserDto myUserDto) {
        MyUser myUser = modelMapper.map(myUserDto, MyUser.class);
//        if (myUserDto.getHeavensOwned() != null) {
//            List<Heaven> heavens = HeavenDto.fromDTO(myUserDto.getHeavensOwned());
//            myUser.setHeavenOwned(heavens);
//            heavens.forEach(heaven -> heaven.setHeavenOwn(myUser));
//        }

        return myUser;
    }

    public static List<MyUser> fromAuthUsers(List<MyUserDto> myUserDtos) {
        return myUserDtos.stream()
                .map(MyUserDto::fromHeavenDto)
                .collect(Collectors.toList());
    }

    public static MyUserDto fromMyUser(MyUser myUser) {
        return modelMapper.map(myUser, MyUserDto.class);
    }
}
