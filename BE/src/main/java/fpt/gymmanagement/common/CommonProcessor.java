package fpt.gymmanagement.common;

public abstract class CommonProcessor<S extends CommonService<E, ID>, T extends CommonTransform<E, DTO>, E, DTO, ID> {

    protected final S service;
    protected final T transform;

    protected CommonProcessor(S service, T transform) {
        this.service = service;
        this.transform = transform;
    }

    public DTO getById(ID id) {
        var e = service.getOrElseThrow(id);
        return (DTO) transform.toDto(e);
    }

    public void deleteById(ID id) {
        service.deleteById(id);
    }

    public DTO saveOrUpdate(DTO dto, ID id) {
        E e = (E) transform.toEntity(dto);
        e = (E) service.saveOrUpdate(e, id);
        return (DTO) transform.toDto(e);
    }
}
