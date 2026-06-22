package com.ideasnap.repository;

import com.ideasnap.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query("SELECT p FROM Post p WHERE p.visibility = 'PUBLIC' AND (p.expiresAt IS NULL OR p.expiresAt > :now)")
    Page<Post> findActivePublicPosts(@Param("now") Instant now, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author.username = :username AND p.visibility = 'PUBLIC' AND (p.expiresAt IS NULL OR p.expiresAt > :now)")
    Page<Post> findActivePublicPostsByAuthor(@Param("username") String username, @Param("now") Instant now, Pageable pageable);

    // Trending Algorithm query: Score = (ReactionsCount * 2 + CommentsCount * 5) / (HoursSinceCreation + 2)^1.5
    // Using a Native SQL query because of advanced math functions like power/extract.
    @Query(value = "SELECT p.* FROM posts p " +
            "LEFT JOIN (SELECT post_id, COUNT(*) as react_count FROM reactions GROUP BY post_id) r ON p.id = r.post_id " +
            "LEFT JOIN (SELECT post_id, COUNT(*) as comment_count FROM comments GROUP BY post_id) c ON p.id = c.post_id " +
            "WHERE p.visibility = 'PUBLIC' AND (p.expires_at IS NULL OR p.expires_at > :now) " +
            "ORDER BY (COALESCE(r.react_count, 0) * 2 + COALESCE(c.comment_count, 0) * 5) / " +
            "POWER(EXTRACT(EPOCH FROM (:now - p.created_at)) / 3600.0 + 2.0, 1.5) DESC, p.created_at DESC",
            countQuery = "SELECT COUNT(*) FROM posts p WHERE p.visibility = 'PUBLIC' AND (p.expires_at IS NULL OR p.expires_at > :now)",
            nativeQuery = true)
    Page<Post> findTrendingPosts(@Param("now") Instant now, Pageable pageable);

    // Search by title (case-insensitive)
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) AND p.visibility = 'PUBLIC' AND (p.expiresAt IS NULL OR p.expiresAt > :now)")
    Page<Post> searchByTitle(@Param("query") String query, @Param("now") Instant now, Pageable pageable);

    // Search by tag
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t) = LOWER(:tag) AND p.visibility = 'PUBLIC' AND (p.expiresAt IS NULL OR p.expiresAt > :now)")
    Page<Post> searchByTag(@Param("tag") String tag, @Param("now") Instant now, Pageable pageable);

    // Search by username
    @Query("SELECT p FROM Post p WHERE LOWER(p.author.username) LIKE LOWER(CONCAT('%', :username, '%')) AND p.visibility = 'PUBLIC' AND (p.expiresAt IS NULL OR p.expiresAt > :now)")
    Page<Post> searchByUsername(@Param("username") String username, @Param("now") Instant now, Pageable pageable);
}
