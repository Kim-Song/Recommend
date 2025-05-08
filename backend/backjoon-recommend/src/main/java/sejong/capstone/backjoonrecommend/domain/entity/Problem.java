package sejong.capstone.backjoonrecommend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Problem {
    @Id
    private Long number;

    private String name;
    private String tier;
    private String algorithm;
    private Double timeCondition;
    private Long memoryCondition;
    private Long submissionCount;
    private Long answerSubmissionCount;
    private Long answeredPeopleCount;
    private Double correctRate;
    private String description;
    private String input;
    private String output;
    @Column(name = "other_result_form_1")
    private Long otherResultForm1;
    @Column(name = "other_result_form_2")
    private Long otherResultForm2;
}
