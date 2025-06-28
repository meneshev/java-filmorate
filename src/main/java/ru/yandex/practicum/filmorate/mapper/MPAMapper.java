package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.model.MPA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MPAMapper {
    public static MPADto mapToMPADto(MPA mpa) {
        MPADto mpaDto = new MPADto();
        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());
        return mpaDto;
    }
}
