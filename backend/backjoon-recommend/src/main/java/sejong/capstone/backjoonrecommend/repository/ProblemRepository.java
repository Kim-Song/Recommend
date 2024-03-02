package sejong.capstone.backjoonrecommend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.backjoonrecommend.domain.entity.Problem;

@Repository
@Transactional
public class ProblemRepository {

    @PersistenceContext
    private EntityManager em;

    public Problem findByProblemNumber(Long problemNumber) {
        return em.createQuery("select m from Problem m where m.number = :problemNumber",
                        Problem.class)
                .setParameter("problemNumber", problemNumber)
                .getSingleResult();
    }

    /**
     * 지원하는 문제인지 확인하는 코드입니다.
     * other_result_form의 값들이 0인 문제만 지원합니다. (백준에서의 비율값, 점수제 문제는 지원하지 않습니다.)
     * 데이터 베이스에 존재하는 문제만 지원합니다.
     * @param problemNumber
     * @return
     */
    public Boolean validateProblemNumber(Long problemNumber) {
        List<Problem> problems = em.createQuery("select m from Problem m "
                                + "where m.number = :problemNumber "
                                + "and m.otherResultForm1 = 0 and m.otherResultForm2 = 0",
                        Problem.class)
                .setParameter("problemNumber", problemNumber)
                .getResultList();
        if (problems.size() == 0) {
            return false;
        }
        return true;
    }

}
