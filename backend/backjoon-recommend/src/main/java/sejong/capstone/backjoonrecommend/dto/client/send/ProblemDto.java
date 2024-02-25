package sejong.capstone.backjoonrecommend.dto.client.send;

import java.util.List;
import lombok.Data;
import sejong.capstone.backjoonrecommend.domain.entity.Problem;

@Data
public class ProblemDto {
    private List<Problem> data;
}
