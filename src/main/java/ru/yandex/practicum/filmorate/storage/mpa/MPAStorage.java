package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    Optional<MPA> getMPAById(Long id);
    List<MPA> getAllMPAs();
}
