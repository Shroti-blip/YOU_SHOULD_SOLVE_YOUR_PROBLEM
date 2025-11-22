package com.example.ProjectHON.Post_Report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport , Integer> {
    boolean existsByReporterUserIdAndPostreport_PostId(Long reporterId, Long postId);

}
