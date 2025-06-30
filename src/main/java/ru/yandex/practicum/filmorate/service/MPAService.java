package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MPADto;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.mapper.MPAMapper;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPAService {
    private final MPAStorage mpaStorage;

    public List<MPADto> findAll() {
        return mpaStorage.getAllMPAs().stream()
                .map(MPAMapper::mapToMPADto)
                .collect(Collectors.toList());
    }

    public MPADto getMPAById(Long id) {
        MPA mpa = mpaStorage.getMPAById(id)
                .orElseThrow(() -> {
                    log.error("MPA not found");
                    return new ObjectNotFoundException("Не удалось найти рейтинг по указанному идентификатору", id);
                });

        return MPAMapper.mapToMPADto(mpa);
    }
}
