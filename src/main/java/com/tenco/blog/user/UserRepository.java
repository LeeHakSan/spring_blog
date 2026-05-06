package com.tenco.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@Repository -> 이미 JpaRepository에서 상속 받고 있음
public interface UserRepository extends JpaRepository<User, Integer> {

    // 1. 사용자 등록 및 수정 : save(User user)
    // - 새로운 사용자를 Insert 하거나, 기존 사용자 정보를 UPDATE 한다.
    // 2. 사용자 단건 조회 : findById(Integer id)
    // PK(id) 를 통해 특정 사용자를 조회 하며 Optional<User>를 반환
    // 3. 전체 사용자 목록 조회 : findAll()
    // - DB에 저장된 모드 사용자 정보를 List<User> 형태로 가지고 온다.
    // 4. 사용자 삭제 기능 : deleteById(Integer id)
    // - 특정 아이디를 가진 사용자를 삭제
    // 5. 데이터 개수 : count()
    // - 전체 레코드 수 반환
    // 6. 존재 여부 확인 : existsById(Integer id)
    // - 해당 ID를 가진 데이터가 있는지 확인 하여 boolean을 반환

    // 사용자 명으로 사용자 조회 (중복 체크 확인용)
    @Query("""
        SELECT u FROM User u WHERE u.username = :username
        """)
    Optional<User> findByUserName(@Param("username") String username);

    // 사용자명과 비밀번호로 사용자 조회(로그인용)
    @Query("""
        SELECT u FROM User u WHERE u.username = :username AND u.password = :password
        """)
    Optional<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);

    // 사용자 정보 수정
    // [더티 체킹이란?]
    // 트랜잭션 내에서 조회된 객체상태를 변경하면
    // 트랜잭션이 끝나는 기점에 JPA가 변경된 내용을 자동으로 감시하여
    // DB에 UPDATE 쿼리를 날려주는 기능을 말한다


}
