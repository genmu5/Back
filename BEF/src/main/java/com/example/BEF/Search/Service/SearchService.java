package com.example.BEF.Search.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.DTO.MapLocationResponse;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.Search.Client.OpenAIClient;
import com.example.BEF.Search.Config.OpenAIClientConfig;
import com.example.BEF.Search.DTO.TranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionResponse;
import com.example.BEF.Search.Repository.SearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final SearchRepository searchRepository;
    private final DisabledRepository disabledRepository;
    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;
    private final LocationRepository locationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<MapLocationResponse> findByKeyword(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        List<Location> locations = searchRepository.findByKeyword(keyword);

        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledRepository.findDisabledByLocation(location);

                    return new MapLocationResponse(location, disabled);
                })
                .collect(Collectors.toList());
    }

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())  // file 필드를 호출
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }

//    // Keyword의 해당하는 단어 탐색하여 음성 텍스트 필터링 Keywords 배열에서 앞에 있을 수록 단어 우선순위가 높다.
//    public String filterTranscription(String transcribedText) {
//        List<String> Keywords = Arrays.asList("공원","바다","서울");
//
//        return Keywords.stream()
//                .filter(transcribedText::contains)
//                .findFirst()
//                .orElse("키워드 없음");
//    }
    // location 테이블에서 키워드에 해당하는 튜플 조회후 리스트 형식으로 반환
//    public List<MapLocationResponse> searchLocationsByKeyword(String transcribedText) {
//        String keyword = filterTranscription(transcribedText);
//
//        if (keyword != null) {
//            List<Location> locations = locationRepository.findDescription(keyword);
//
//            // Location 리스트를 MapLocationResponse 리스트로 변환
//            return locations.stream()
//                    .map(location -> {
//                        Disabled disabled = disabledRepository.findDisabledByLocation(location);
//                        return new MapLocationResponse(location, disabled);
//                    })
//                    .collect(Collectors.toList());
//        } else {
//            return new ArrayList<>();  // 키워드가 없을 경우 빈 리스트 반환
//        }
//    }

    public List<String> extractKeywordsFromText(String transcribedText) {
        return NLPKeywordExtractor.extractKeywords(transcribedText);
    }

    public List<MapLocationResponse> searchLocationsByKeyword(String transcribedText) {

        List<String> keywords = extractKeywordsFromText(transcribedText);

        if (keywords.isEmpty()) {
            return new ArrayList<>();  // 키워드가 없으면 빈 리스트 반환
        }

        List<Location> locations = searchLocationsDynamically(keywords);

        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledRepository.findDisabledByLocation(location);
                    return new MapLocationResponse(location, disabled);
                })
                .collect(Collectors.toList());
    }


    private List<Location> searchLocationsDynamically(List<String> keywords) {
        if (keywords.isEmpty()) {
            return Collections.emptyList();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = cb.createQuery(Location.class);
        Root<Location> root = query.from(Location.class);

        List<Predicate> predicates = new ArrayList<>();

        for (String keyword : keywords) {
            Predicate addrMatch = cb.like(cb.lower(root.get("addr")), "%" + keyword.toLowerCase() + "%");
            Predicate descriptionMatch = cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");

            predicates.add(cb.or(addrMatch, descriptionMatch));
        }

        query.select(root)
                .where(cb.or(predicates.toArray(new Predicate[0])))
                .groupBy(root.get("id"))
                .having(cb.ge(cb.count(root.get("id")), 1));

        List<Location> results = entityManager.createQuery(query)
                .setMaxResults(100)
                .getResultList();

        results.sort(Comparator.comparing((Location loc) -> keywords.stream().filter(kw -> loc.getAddr().toLowerCase().contains(kw)).count()).reversed());

        return results;
    }



    public List<Location> findLocationWithRadius(double lat, double lng){
        return searchRepository.findLocationsWithinRadius(lat, lng);
    }
}