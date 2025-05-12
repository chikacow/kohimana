package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SearchRepository {
    private static final String LIKE_FORMAT = "%%%s%%";

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Yet to implement sort functions
     * Bugs with total page
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param search
     * @return
     */
    public PageResponse<?> getAllUsersWithSortByMultipleColumnsAndSearch(int pageNo, int pageSize, String sortBy, String search) {

        StringBuilder sqlQuery = new StringBuilder(
                "SELECT new com.chikacow.kohimana.dto.response.UserResponseDTO(" +
                        "u.firstName, u.lastName, u.email, u.phoneNumber, " +
                        "u.dateOfBirth, u.gender, u.username, u.password, u.createdAt) " +
                        "FROM User u " +
                        "WHERE 1=1 "
        );


        if (search != null && !search.isEmpty()) {

            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");

        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if (search != null && !search.isEmpty()) {

            selectQuery.setParameter("firstName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("lastName", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("email", String.format(LIKE_FORMAT, search));

        }

        selectQuery.setFirstResult((pageNo - 1) * pageSize);
        selectQuery.setMaxResults(pageSize);
        List<User> users = selectQuery.getResultList();

        System.out.println(users);


        // Count users
        StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) FROM User u");
        if (org.springframework.util.StringUtils.hasLength(search)) {
            sqlCountQuery.append(" WHERE lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" OR lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" OR lower(u.email) like lower(?3)");
        }

        Query countQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            countQuery.setParameter(1, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(2, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(3, String.format(LIKE_FORMAT, search));
            countQuery.getSingleResult();
        }

        Long totalElements = (Long) countQuery.getSingleResult();
        System.out.println(totalElements);
        log.info("totalElements={}", totalElements);

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<?> page = new PageImpl<>(users, pageable, totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(users)
                .build();


    }
}
