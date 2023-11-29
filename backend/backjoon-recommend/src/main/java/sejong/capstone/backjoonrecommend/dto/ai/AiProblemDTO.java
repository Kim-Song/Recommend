package sejong.capstone.backjoonrecommend.dto.ai;

import lombok.Data;

@Data
public class AiProblemDTO {
    private String problem_context;
    private String problem_input;
    private String problem_output;
}
