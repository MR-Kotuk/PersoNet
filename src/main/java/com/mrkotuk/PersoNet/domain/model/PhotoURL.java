package com.mrkotuk.PersoNet.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PhotoURL {
    private String url;

    @JsonValue
    public String getUrl() {
        return url;
    }

    @JsonCreator
    public static PhotoURL fromUrl(String url) {
        return new PhotoURL(url);
    }
}
