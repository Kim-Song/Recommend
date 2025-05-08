package sejong.capstone.backjoonrecommend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.capstone.backjoonrecommend.domain.entity.Video;

@Repository
@Transactional
public class VideoRepository {
    @PersistenceContext
    private EntityManager em;

    public List<Video> findByAlgorithm(String algorithm) {
        return em.createQuery("select m from Video m where m.algorithm = :algorithm",
                        Video.class)
                .setParameter("algorithm", algorithm)
                .getResultList();
    }
}
