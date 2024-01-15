package fpt.gymmanagement.common;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Transactional
public abstract class CommonJPAServiceImpl<T extends CommonEntity, ID extends Serializable, R extends JpaRepository<T, ID>, Q>
        implements CommonService<T, ID> {
    protected final EntityManager em;
    protected JPAQuery<T> query;
    protected final Q q;
    protected final R repo;

    public CommonJPAServiceImpl(EntityManager em, Q q, R repo) {
        this.em = em;
        this.q = q;
        this.repo = repo;
    }

    @Override
    public Optional<T> getById(ID id) {
        return repo.findById(id);
    }

    @Override
    public T getOrElseThrow(ID id) {
        return getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, notFoundMessage()));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public T saveOrUpdate(T t, ID id) {
        try {
            if (id != null) {
                T e = getOrElseThrow(id);
                this.copyProperties(t, e, "id", "createdDate", "updatedDate");
                return repo.save(e);
            }
            this.copyProperties(null, t, "createdDate", "updatedDate");
            return repo.save(t);
        } catch (Exception e) {
            log.error("SAVE OR UPDATE ERROR {}: {}", t.getClass().getName(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteById(ID id) {
        T t = getOrElseThrow(id);
        repo.deleteById(id);
        log.info("DELETE {}: {}", t.getClass().getName(), t);
    }

    public void copyProperties(T s, T t, String... ignoreProperties) {
        if (s != null) {
            BeanUtils.copyProperties(s, t, ignoreProperties);
        }
        if (t.getCreatedDate() == null) {
            t.setCreatedDate(new Date().getTime());
        }
        t.setUpdatedDate(new Date().getTime());
    }

    protected abstract String notFoundMessage();
}
