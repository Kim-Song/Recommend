package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Video;
import sejong.capstone.backjoonrecommend.dto.client.VideoLinkClientDto;
import sejong.capstone.backjoonrecommend.repository.VideoRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class VideoController {

    private final VideoRepository videoRepository;

    public VideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping("/video")
    private VideoLinkClientDto getVideo(@RequestParam String id) {
        List<Video> videos = videoRepository.findByAlgorithm("데이크스트라");
        VideoLinkClientDto videoLinkClientDto = new VideoLinkClientDto();
        List<String> data = new ArrayList<>();
        for (Video v : videos) {
            String url = v.getUrl();
            data.add(url);
        }
        videoLinkClientDto.setData(data);
        return videoLinkClientDto;
    }
}
