package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Entry extends BaseEntity {
    @Id
    @Column(name = "entry_id")
    protected String id;

    protected String title;

    protected String disambiguation;

    protected LocalDate date;

    @OneToMany(mappedBy = "entry", orphanRemoval = true)
    protected Set<Credit> credits = new HashSet<>();

    @OneToMany(mappedBy = "entry")
    protected List<Review> reviews = new ArrayList<>();

    public Entry(String id) {
        this.id = id;
    }

    public void addCredit(Credit credit) {
        credits.add(credit);
    }

    public void removeCredit(Credit credit) {
        credits.remove(credit);
    }

    public Long getReviewCount() {
        return (long) reviews.size();
    }

    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setEntry(this);
    }
}
