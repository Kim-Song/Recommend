package sejong.capstone.backjoonrecommend.dto.client;

import java.util.List;
import lombok.Data;
import sejong.capstone.backjoonrecommend.domain.VideoLink;

@Data
public class VideoLinkClientDto {
    private List<VideoLink> data;
}
