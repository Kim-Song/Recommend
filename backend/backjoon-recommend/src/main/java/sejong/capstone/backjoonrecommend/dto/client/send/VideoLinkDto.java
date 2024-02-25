package sejong.capstone.backjoonrecommend.dto.client.send;

import java.util.List;
import lombok.Data;
import sejong.capstone.backjoonrecommend.dto.VideoLink;

@Data
public class VideoLinkDto {
    private List<VideoLink> data;
}
