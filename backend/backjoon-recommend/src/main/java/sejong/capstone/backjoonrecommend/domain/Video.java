package sejong.capstone.backjoonrecommend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Video {
    @Id
    @GeneratedValue
    private Long id;
    private String algorithm;
    private String url;
}
