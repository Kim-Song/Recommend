package sejong.capstone.backjoonrecommend.dto.ai.receipt;

import java.util.List;
import lombok.Data;

@Data
public class AiDTO {
    private List<Long> recommend_list;
    private List<String> user_weak_algorithm;
}
