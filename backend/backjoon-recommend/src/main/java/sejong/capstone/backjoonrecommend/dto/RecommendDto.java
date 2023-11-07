package sejong.capstone.backjoonrecommend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendDto {
    private Long problemId;
    private String problemName;

    public RecommendDto(Long problemId, String problemName) {
        this.problemId = problemId;
        this.problemName = problemName;
    }
}
