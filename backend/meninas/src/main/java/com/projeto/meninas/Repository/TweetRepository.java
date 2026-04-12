package com.projeto.meninas.Repository;

import com.projeto.meninas.Entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
