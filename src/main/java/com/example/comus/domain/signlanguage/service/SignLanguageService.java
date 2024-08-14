package com.example.comus.domain.signlanguage.service;

import com.example.comus.domain.signlanguage.dto.response.SignLanguageInfoResponseDto;
import com.example.comus.domain.signlanguage.entity.SignLanguage;
import com.example.comus.domain.signlanguage.repository.SignLanguageRepository;
import com.example.comus.global.error.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.comus.global.error.ErrorCode.SIGNLANGUAGE_NOT_FOUND;

@AllArgsConstructor
@Service
@Transactional
public class SignLanguageService {
    private final SignLanguageRepository signLanguageRepository;

    public List<SignLanguageInfoResponseDto> getSignLanguage(String answer) {
        List<SignLanguageInfoResponseDto> result = new ArrayList<>();

        String[] words = answer.split("\\s+");

        for (String word : words) {
            List<SignLanguage> signLanguages = signLanguageRepository.findBySignLanguageName(word);
            for (SignLanguage signLanguage : signLanguages) {
                SignLanguageInfoResponseDto dto = new SignLanguageInfoResponseDto(
                        signLanguage.getId(),
                        signLanguage.getSignLanguageName(),
                        signLanguage.getSignLanguageVideoUrl(),
                        signLanguage.getSignLanguageDescription()
                );
                result.add(dto);
            }
        }

        return result;
    }

    public String crawlVideoUrls(String searchWord) {
        try {
            // 검색 페이지 URL
            String searchUrl = "https://sldict.korean.go.kr/front/search/searchAllList.do?searchKeyword=" + searchWord;

            // 검색 페이지에서 첫 번째 결과 페이지로 이동
            Document doc = Jsoup.connect(searchUrl).get();
            Elements spanElements = doc.select("span[class=tit]");
            Element spanElement = spanElements.first();
            Element aElement = spanElement.selectFirst("a");
            String aElementText = aElement.text().trim();
            if (!aElementText.contains(searchWord)) {
                throw new EntityNotFoundException(SIGNLANGUAGE_NOT_FOUND);
            }

            String href = aElement.attr("href");

            // fnSearchContentsView 인자 추출
            String[] argsArray = href.split("'");
            String originNo = argsArray[1];
            String topCategory = argsArray[2];

            // URL 생성
            String url = "https://sldict.korean.go.kr/front/sign/signContentsView.do" +
                    "?origin_no=" + originNo +
                    "&top_category=" + topCategory +
                    "&category=" +
                    "&searchKeyword=" + URLEncoder.encode(searchWord, "UTF-8") +
                    "&searchCondition=" +
                    "&search_gubun=" +
                    "&museum_type=00" +
                    "&current_pos_index=0";

            // 상세 페이지로 이동하여 수형 설명 & 비디오 URL 추출
            Document detailDoc = Jsoup.connect(url).get();
            Elements ddElements = detailDoc.select("dl.content_view_dis > dd");
            if (ddElements.size() < 2) {
                System.out.println("Description not found");
                return null;
            }
            Element descriptionElement = ddElements.get(1);
            String descriptionText = descriptionElement.text();
            System.out.println("Description: " + descriptionText);

            System.out.println(detailDoc.html());
            Element videoArea = detailDoc.getElementById("videoArea");
            System.out.println(videoArea.html());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}



