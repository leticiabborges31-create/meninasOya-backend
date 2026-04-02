package com.projeto.meninas.Repository;


import com.projeto.meninas.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TweetRepository extends JpaRepository<Usuario, UUID>{

}
