package com.heavens.stream.repositories;

import com.heavens.stream.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    // You can add custom query methods here if needed
}
