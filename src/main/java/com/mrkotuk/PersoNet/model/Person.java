package com.mrkotuk.PersoNet.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.service.GoogleDriveService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Component
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personId;
    private String email;

    @Builder.Default
    private PhotoURL previewPhotoUrl = new PhotoURL(GoogleDriveService.DEFAULT_PHOTO_URL);

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "person_photo_urls", joinColumns = @JoinColumn(name = "person_id"))
    private List<PhotoURL> photoURLs = new ArrayList<>();

    private String creationDate;
    private PersonType personType;
    private PersonStatus personStatus;

    @Builder.Default
    private HashSet<String> tags = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private List<LineTemplate> lineTemplates;
}