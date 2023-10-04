package com.heavens.stream.dtos;

import com.heavens.stream.models.Actionable;
import com.heavens.stream.models.Heaven;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeavenDto  extends Actionable implements Serializable {

    private Long id;

    private String heavenName;

    private String heavenDescription;

    private String stringId;

    private String imageUrl;

    private Long heavenOwn;


    private static final ModelMapper modelMapper = new ModelMapper();

    public static HeavenDto toDTO(Heaven heaven) {
        return modelMapper.map(heaven, HeavenDto.class);
    }

    public static Heaven fromDTO(HeavenDto heavenDTO) {
        return modelMapper.map(heavenDTO, Heaven.class);
    }

    public static List<HeavenDto> toDTO(List<Heaven> heavens) {
        return heavens.stream().map(HeavenDto::toDTO).collect(Collectors.toList());
    }

    public static List<Heaven> fromDTO(List<HeavenDto> heavenDtos) {
        return heavenDtos.stream().map(HeavenDto::fromDTO).collect(Collectors.toList());
    }
}

