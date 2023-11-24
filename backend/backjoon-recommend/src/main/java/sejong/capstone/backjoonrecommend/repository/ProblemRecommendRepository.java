package sejong.capstone.backjoonrecommend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.backjoonrecommend.domain.Problem;

@Repository
@Transactional
public class ProblemRecommendRepository {

    @PersistenceContext
    private EntityManager em;

    public Problem findByProblemNumber(Long problemNumber) {
        return em.createQuery("select m from Problem m where m.number = :problemNumber",
                        Problem.class)
                .setParameter("problemNumber", problemNumber)
                .getSingleResult();
    }
}
