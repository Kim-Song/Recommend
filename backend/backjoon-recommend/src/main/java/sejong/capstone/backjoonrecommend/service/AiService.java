package sejong.capstone.backjoonrecommend.service;

import java.util.List;
import sejong.capstone.backjoonrecommend.dto.ai.receipt.AiDTO;

public interface AiService {
    public AiDTO getProblems(String id, List<String> algorithms);
    public AiDTO getVideos(String id);
}
