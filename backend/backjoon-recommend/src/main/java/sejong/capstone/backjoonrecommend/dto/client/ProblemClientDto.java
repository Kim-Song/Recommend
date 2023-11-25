package sejong.capstone.backjoonrecommend.dto.client;

import java.util.List;
import lombok.Data;
import sejong.capstone.backjoonrecommend.domain.Problem;

@Data
public class ProblemClientDto {
    private List<Problem> data;
}
