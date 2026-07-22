package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public Optional<Post> findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        SortOrder sortOrder = SortOrder.from(sort);

        if (sortOrder == null) {
            throw new ParameterNotValidException(
                    "sort",
                    "Получено: " + sort + ", должно быть: asc или desc"
            );
        }

        if (size <= 0) {
            throw new ParameterNotValidException(
                    "size",
                    "Некорректный размер выборки. Размер должен быть больше нуля"
            );
        }

        if (from < 0) {
            throw new ParameterNotValidException(
                    "from",
                    "Начало выборки должно быть положительным числом"
            );
        }

        return postService.findAll(sortOrder, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}