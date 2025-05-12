package com.example.isip.socialmedia;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    // No additional code needed unless you want custom queries.
}