package com.tenco.blog.board;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller // IoC
@RequiredArgsConstructor // DI
public class BoardController {
    // DI
    private final BoardNativeRepository boardNativeRepository;
    private final BoardPersistRepository boardPersistRepository;

    /**
     * 게시글 작성 화면 요청
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save-form
     */
    @GetMapping("/board/save-form")
    public String saveForm() {

        return "board/save-form";
    }

    /**
     * 게시글 작성 기능 요청
     * @return 페이지 반환
     * 주소설계 : http://localhost:8080/board/save-form
     */
    @PostMapping("/board/save")
    // 사용자 요청 -> HTTP 요청 메시지(Post)
    public String saveProc(BoardRequest.SaveDTO saveDTO) {
        Board board = saveDTO.toEntity();
        boardPersistRepository.save(board);
        return "redirect:/";
    }


    /**
     * 게시글 목록 화면 요청
     * 주소설계 : http://localhost:8080/
     */
    @GetMapping({"/", "index"})
    public String list(Model model) {
        List<Board> boardList = boardPersistRepository.findAll();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }


    // 게시글 상세보기 화면 요청
    // http://localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detailPage(@PathVariable(name = "id") Integer id, Model model) {
        // 유효성 검사 , 인증 검사

        Board board = boardPersistRepository.findById(id);

        // board는 연관관계가 User 엔티티와 ManyToOne 관계 설정 돼 있다
        // 직접 쿼리 구문을 작성하지 않을 때 즉, 엔티티 매니저 메서드로 객체를 조회시
        // 자동으로 join 구문을 호출해준다
        // 단 Fetch 전략에 따라 EAGER, LAZY 전략에 따라 한 번에 다 조인해서 가져오거나
        // 필요할 때 한 번 더 요청 하는 것이 LAZY 전략
        // 코드상에서 User 정보 공유 LAZY 전략
        // System.out.println(board.getUser().getUsername());

        model.addAttribute("board", board);

        return "board/detail";
    }


    // /board/{{board.id}}/delete
    @PostMapping("/board/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Integer id) {
        boardPersistRepository.deleteById(id);

        // PRG 패턴( Post-> Redirect -> Get) 적용
        return "redirect:/";
    }


    // http://localhost:8080/board/1/update-form
    @GetMapping("/board/{id}/update-form")
    public String updateFormPage(@PathVariable(name = "id") Integer id, Model model) {
        // 사용자 에게 해당 게시물 내용을 보여 줘야 한다.

        // 조회 기능 - 게시글 id로
        Board board = boardPersistRepository.findById(id);
        model.addAttribute("board", board);

        return "board/update-form";
    }

    // /board/{id}/update
    @PostMapping("/board/{id}/update")
    // 메세지 컨버터라는 객체가 동작해서 자동으로 객체를 생성하고 값을 매핑해준다.
    public String updateProc(@PathVariable(name = "id") Integer id,
                             BoardRequest.UpdateDTO updateDTO) {
        // 1. 유효성 검사
        // username, title, content 유효성 검사
        updateDTO.validate();

        // 2. DAO 계층으로 전달
        boardPersistRepository.updateById(id, updateDTO);

        return "redirect:/board/" + id;
    }

}
