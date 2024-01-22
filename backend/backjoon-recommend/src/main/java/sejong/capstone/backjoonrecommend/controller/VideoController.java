package sejong.capstone.backjoonrecommend.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.capstone.backjoonrecommend.domain.Video;
import sejong.capstone.backjoonrecommend.domain.VideoLink;
import sejong.capstone.backjoonrecommend.dto.ai.AiDTO;
import sejong.capstone.backjoonrecommend.dto.client.VideoLinkClientDto;
import sejong.capstone.backjoonrecommend.repository.VideoRepository;
import sejong.capstone.backjoonrecommend.service.AiService;

@RestController
public class VideoController {

    private final VideoRepository videoRepository;
    private final AiService aiService;

    public VideoController(VideoRepository videoRepository, AiService aiService) {
        this.videoRepository = videoRepository;
        this.aiService = aiService;
    }

    @GetMapping("/video")
    private VideoLinkClientDto getVideo(@RequestParam String id) {
        // id를 가지고 취약 알고리즘을 알아낸다.
        AiDTO analysis = aiService.getProblemList(id);
        List<String> userWeakAlgorithms = analysis.getUser_weak_algorithm();

        ArrayList<VideoLink> videoLinks = new ArrayList<>();

        for (String algo : userWeakAlgorithms) {
            List<Video> videos = videoRepository.findByAlgorithm(algo);
            System.out.println(videos);

            for (Video ele : videos) {
                VideoLink videoLink = new VideoLink();
                String algorithm = ele.getAlgorithm();
                String url = ele.getUrl();
                videoLink.setWeakAlgorithm(algorithm);
                videoLink.setLink(url);
                System.out.println(videoLink);
                videoLinks.add(videoLink);
            }
        }

        System.out.println(videoLinks);
        VideoLinkClientDto videoLinkClientDto = new VideoLinkClientDto();
        videoLinkClientDto.setData(videoLinks);
        return videoLinkClientDto;
    }
}
