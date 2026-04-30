package com.tenco.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// SRP - 단일 책임 원칙
@Repository // IoC 대상
@RequiredArgsConstructor
public class UserRepository {

    // @Autowired // DI - 스프링 프레임 워크가 주소값 자동 주입
    private final EntityManager em;

    public User findById(Integer id) {
        User user = em.find(User.class, id);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }

    @Transactional
    // 회원가입 요청 시 --> insert
    public User save(User user) {
        // 매개변수로 들어온 User Object는 비 영속 상태이다
        em.persist(user);
        // 리턴시 User Object 영속화 된 상태이다.
        return user;
    }

    // 사용자 이름 중복 확인
    public User findByUsername(String username) {
        String jpqlStr = """
                SELECT u FROM User u WHERE u.username = :username
                """;

        try {
            return em.createQuery(jpqlStr, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // 로그인 요청 --> select
    public User findByUsernameAndPassword(String username, String password) {
        String jpqlStr = """
                SELECT u FROM User u WHERE u.username = :username AND u.password = :password
                """;

        try {
            return em.createQuery(jpqlStr, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }

    }

    @Transactional
    public User updateById(Integer id, UserRequest.UpdateDTO updateDTO) {
        //
        User userEntity = findById(id); // 영속성 컨텍스트에 관리되는 엔티티
        userEntity.setPassword(updateDTO.getPassword()); // 객체 상태값 변경함 - password
        return userEntity;
    }
}
