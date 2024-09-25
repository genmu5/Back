package com.example.BEF.Location.Domain;

import com.example.BEF.Area.Domain.Area;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Location {

    @Id
    @Column(name = "content_id")
    private Long contentId;   // 콘텐츠 ID

    @ManyToOne
    @JoinColumn(name = "area_code")
    private Area area;        // 외래 키 (Area와의 관계)

    @Column(name = "content_type_id")
    private Long contentTypeId;  // 관광 타입 ID
    @Column(name = "content_title")
    private String contentTitle; // 콘텐츠 제목
    @Column(name = "addr")
    private String addr;         // 주소
    @Column(name = "detail_addr")
    private String detailAddr;   // 상세 주소
    @Column(name = "gps_x")
    private Double gpsX;         // GPS X 좌표
    @Column(name = "gps_y")
    private Double gpsY;         // GPS Y 좌표
    @Column(name = "description")
    private String description;  // 개요
    @Column(name = "phone_number")
    private String phoneNumber;  // 전화번호
    @Column(name = "original_image")
    private String originalImage; // 원본 이미지
    @Column(name = "thumbnail_image")
    private String thumbnailImage; // 썸네일 이미지

}
