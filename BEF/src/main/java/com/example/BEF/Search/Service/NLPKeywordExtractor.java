package com.example.BEF.Search.Service;

import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import scala.collection.Seq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NLPKeywordExtractor {
    private static List<String> stopwords;


    static {
        try {
            InputStream inputStream = NLPKeywordExtractor.class.getClassLoader().getResourceAsStream("stopword.txt");

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                stopwords = reader.lines().collect(Collectors.toList());
            } else {
                throw new Exception("stopword.txt not found");
            }
        } catch (Exception e) {
            stopwords = Arrays.asList(
                    "있는", "추천해", "검색해", "어디", "좋은", "많은", "대한", "관련", "것", "이번", "지난",
                    "하는", "해줘", "해", "대한", "모든", "다양한", "속", "같은", "가장", "등", "및"
            );
        }
    }

    public static List<String> extractKeywords(String transcribedText) {
        Seq<com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken> tokens =
                TwitterKoreanProcessorJava.tokenize(transcribedText);

        List<String> keywords = TwitterKoreanProcessorJava.tokensToJavaStringList(tokens).stream()
                .filter(word -> word.length() > 1)
                .collect(Collectors.toList());

        List<String> filteredKeywords = keywords.stream()
                .filter(word -> !stopwords.contains(word))
                .collect(Collectors.toList());

        return filteredKeywords;
    }

}