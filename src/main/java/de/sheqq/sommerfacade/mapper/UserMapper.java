package de.sheqq.sommerfacade.mapper;

import de.sheqq.sommerfacade.dto.User;
import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDO toEntity(User dto) {
        if (dto == null) return null;
        UserDO u = new UserDO();
        u.setId(dto.getId());
        u.setUsername(dto.getUsername());
        u.setMember(listLocalDateToArrayLocalDateTime(dto.getMember()));
        u.setLevel(dto.getLevel());
        u.setColor(dto.getColor());
        return u;
    }

    public User toDto(UserDO entity) {
        if (entity == null) return null;
        User dto = new User();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setMember(arrayLocalDateTimeToListLocalDate(entity.getMember()));
        dto.setLevel(entity.getLevel());
        dto.setColor(entity.getColor());
        return dto;
    }

    private LocalDateTime[] listLocalDateToArrayLocalDateTime(List<LocalDate> dates) {
        if (dates == null) return null;
        return dates.stream()
                .map(d -> d.atStartOfDay())
                .toArray(LocalDateTime[]::new);
    }

    private List<LocalDate> arrayLocalDateTimeToListLocalDate(LocalDateTime[] arr) {
        if (arr == null) return null;
        return Arrays.stream(arr)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toList());
    }
}