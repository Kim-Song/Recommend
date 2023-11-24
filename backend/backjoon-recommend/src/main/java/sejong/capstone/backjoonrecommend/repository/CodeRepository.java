package sejong.capstone.backjoonrecommend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.backjoonrecommend.domain.Code;

@Repository
@Transactional
public class CodeRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Code code) {
        em.persist(code);
    }
}
