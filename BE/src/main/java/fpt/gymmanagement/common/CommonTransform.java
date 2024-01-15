package fpt.gymmanagement.common;

public interface CommonTransform<Entity, DTO> {
    Entity toEntity(DTO dto);

    DTO toDto(Entity e);
}
