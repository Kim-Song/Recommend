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

    public Code getBestCodeByBigO(Long problemNumber, String language) {
        return em.createQuery(
                        "select m from Code m where m.number = :problemNumber and m.language = :language order by m.time asc",
                        Code.class)
                .setParameter("problemNumber", problemNumber)
                .setParameter("language", language)
                .getResultList()
                .get(0);
    }

    public Code getBestCodeBySpaceComplexity(Long problemNumber, String language) {
        return em.createQuery(
                        "select m from Code m where m.number = :problemNumber and m.language = :language order by m.time asc",
                        Code.class)
                .setParameter("problemNumber", problemNumber)
                .setParameter("language", language)
                .getResultList()
                .get(0);
    }
}
