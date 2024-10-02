package com.nbe2.api.post;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.nbe2.api.global.dto.Response;
import com.nbe2.api.post.dto.PostRegisterRequest;
import com.nbe2.api.post.dto.PostUpdateRequest;
import com.nbe2.common.annotation.PageDefault;
import com.nbe2.common.dto.Page;
import com.nbe2.common.dto.PageResult;
import com.nbe2.domain.posts.entity.City;
import com.nbe2.domain.posts.service.PostService;
import com.nbe2.domain.posts.service.dto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApi {

    private final PostService postService;

    // ToDo : SpringSecurity 의 @AuthenticationPrincipal 사용
    @PostMapping
    public Response<Long> postPost(
            @RequestBody @Validated final PostRegisterRequest request,
            //            , @AuthenticationPrincipal Long id    (userId)
            @RequestParam("id") final Long id) {
        Long postId =
                postService.save(
                        id,
                        PostDefaultInfo.create(request.title(), request.content(), request.city()));
        return Response.success(postId);
    }

    @GetMapping
    public Response<PageResult<PostListInfo>> getLocalPostPage(
            @RequestParam("city") City city, @PageDefault Page page) {
        PageResult<PostListInfo> postPage = postService.findListPageByCity(page, city);
        return Response.success(postPage);
    }

    // TODO : 1. UserApi로 이동
    // 내 게시글 조회
    // @GetMapping("/api/v1/my/posts")
    // TODO : 2. SpringSecurity 의 @AuthenticationPrincipal 사용
    @GetMapping("/my")
    public Response<PageResult<PostListInfo>> getMyPostPage(
            @RequestParam("id")
                    final Long id
                            //            , @AuthenticationPrincipal Long id (userId)
                            ,
            @PageDefault final Page page) {
        PageResult<PostListInfo> postPage = postService.findListPageByUserId(page, id);
        return Response.success(postPage);
    }

    @GetMapping("/{postsId}")
    public Response<PostDetailsInfo> getPostDetails(@PathVariable("postsId") final Long postsId) {
        PostDetailsInfo postDetails = postService.findDetails(postsId);
        return Response.success(postDetails);
    }

    @PutMapping("/{postsId}")
    public Response<Long> putPost(
            @PathVariable("postsId") final Long postsId,
            @RequestBody @Validated final PostUpdateRequest request) {
        Long postId =
                postService.update(
                        postsId,
                        PostDefaultInfo.create(request.title(), request.content(), request.city()));
        return Response.success(postId);
    }

    @DeleteMapping("/{postsId}")
    public Response<Void> deletePost(@PathVariable("postsId") final Long postsId) {
        postService.delete(postsId);
        return Response.success();
    }
}