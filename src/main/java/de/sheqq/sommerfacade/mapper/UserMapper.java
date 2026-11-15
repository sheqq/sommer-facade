package de.sheqq.sommerfacade.mapper;

import de.sheqq.sommerfacade.dto.User;
import de.sheqq.sommerfacade.infrastructur.model.UserDO;
import de.sheqq.sommerfacade.infrastructur.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDO toEntity(User dto) {
        if (dto == null) return null;
        UserDO u = new UserDO();
        u.setId(dto.getId());
        u.setUsername(dto.getUsername());
        return u;
    }

    public User toDto(UserDO entity) {
        return entity == null ? null : toDto(entity.getId(), entity.getUsername());
    }

    public UserDO toDomain(UserEntity entity) {
        if (entity == null) return null;
        UserDO domain = new UserDO();
        domain.setId(entity.getId());
        domain.setUsername(entity.getUsername());
        return domain;
    }

    public UserEntity toPersistence(UserDO domain) {
        if (domain == null) return null;
        return new UserEntity(domain.getId(), domain.getUsername());
    }

    public User toDto(UserEntity entity) {
        if (entity == null) return null;
        return toDto(entity.getId(), entity.getUsername());
    }

    private User toDto(String id, String username) {
        User dto = new User();
        dto.setId(id);
        dto.setUsername(username);
        return dto;
    }
}