package sejong.capstone.backjoonrecommend.domain.entity;

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
    private Double time_condition;
    private Long memory_condition;
    private Long submission_count;
    private Long answer_submission_count;
    private Long answered_people_count;
    private Double correct_rate;
    private String description;
    private String input;
    private String output;
    private Long other_result_form_1;
    private Long other_result_form_2;
}
