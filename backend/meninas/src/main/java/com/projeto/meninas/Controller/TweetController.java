package com.projeto.meninas.Controller;

import com.projeto.meninas.Controller.dto.CreateTweetDto;
import com.projeto.meninas.Entity.Tweet;
import com.projeto.meninas.Repository.TweetRepository;
import com.projeto.meninas.Repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UsuarioRepository usuarioRepository;

    public TweetController(TweetRepository tweetRepository,
                           UsuarioRepository usuarioRepository) {
        this.tweetRepository = tweetRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@Valid @RequestBody CreateTweetDto dto,
                                            JwtAuthenticationToken token) {
        var usuario = usuarioRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao encontrado"));

        var tweet = new Tweet();
        tweet.setUsuario(usuario);
        tweet.setContent(dto.content());
        tweetRepository.save(tweet);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
