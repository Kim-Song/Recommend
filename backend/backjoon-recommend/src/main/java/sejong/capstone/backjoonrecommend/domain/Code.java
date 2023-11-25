package sejong.capstone.backjoonrecommend.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Code {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long number;
    private Long time;
    private Long memory;
    private String language;
    @Column(columnDefinition = "TEXT")
    private String code;
}
