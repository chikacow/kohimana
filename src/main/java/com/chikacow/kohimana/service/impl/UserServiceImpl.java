package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.model.rbac.Role;
import com.chikacow.kohimana.repository.SearchRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
import com.chikacow.kohimana.util.enums.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chikacow.kohimana.util.AppConst.SORT_BY;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;


    /**
     *Return UserDetails object used for securities, should be found in PreFilter and Jwt handlings
     * @return
     */
    @Override
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserDetails j = null;
                try {
                    UserDetails u = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("username not found"));
                    System.out.println(u.getUsername());
                    System.out.println(u.getPassword());
                    j = u;
                } catch (UsernameNotFoundException e) {
                    System.out.println("oh no");
                }
                //log.info(j.getAuthorities().toString());
                return j;
            }
        };
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    /**
     * Find User object by their email, only one record returned as the requirements
     * @return User object
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }

    /**
     * Find User object by their username, only one record returned as the requirements
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    /**
     * pending to the next version...
     * potentially be a special utility for user
     * @param user
     * @return
     */
    @Override
    public User updateUser(User user) {
        return null;
    }


    /**
     * Return pages presenting list of all existing user in the system
     * allowing specification of customizing page size, sort by asc or desc and select page number to view
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    @Override
    public PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy) {
        int realPageNo = 0;
        if (pageNo > 0) {
            realPageNo = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(realPageNo, pageSize, Sort.by(sorts));

        Page<User> page = userRepository.findAll(pageable);
        List<UserResponseDTO> resList = page.stream().map(user -> UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .password(user.getPassword())
                .username(user.getUsername())
                .build()).toList();

        return PageResponse.builder()
                .items(resList)
                .pageNo(realPageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .build();
    }

    /**
     * Same to the one above but have allowance to sort by multiple selections
     * @param pageNo
     * @param pageSize
     * @param sorts
     * @return
     */
    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int realPageNo = 0;
        if (pageNo > 0) {
            realPageNo = pageNo - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (sorts != null) {
            for (String sortBy : sorts) {
                log.info("sortBy: {}", sortBy);
                // firstName:asc|desc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }

        Pageable pageable = PageRequest.of(realPageNo, pageSize, Sort.by(orders));

        Page<User> page = userRepository.findAll(pageable);
        List<UserResponseDTO> resList = page.stream().map(user -> UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .password(user.getPassword())
                .username(user.getUsername())
                .build()).toList();

        return PageResponse.builder()
                .items(resList)
                .pageNo(realPageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .build();

    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param search
     * @return
     */
    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumnsAndSearch(int pageNo, int pageSize, String sortBy, String search) {
        return searchRepository.getAllUsersWithSortByMultipleColumnsAndSearch(pageNo, pageSize, sortBy, search);
    }

    @Override
    public User getUserByUsernameAndRole(String username, Integer roleID) {
        return userRepository.findByUsernameAndRole(username, roleID).orElseThrow(() -> new ResourceNotFoundException("no user with given username and role"));
    }

    @Override
    public User getUserByStaffId(Long staffId) {
        return userRepository.findByStaffId(staffId).orElseThrow(() -> new ResourceNotFoundException("no user with given staff id"));
    }


    /**
     * A function for user to view their own info
     * This sensitively limit the amount of information from the system that the customer can view
     * @param id
     * @return
     */
    @Override
    public UserResponseDTO getUserInfo(Long id) {
        if (!checkAuthorization(id)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }


        User retrivedUser =  getUserById(id);
        UserResponseDTO res = UserResponseDTO.builder()
                .firstName(retrivedUser.getFirstName())
                .lastName(retrivedUser.getLastName())
                .email(retrivedUser.getEmail())
                .gender(retrivedUser.getGender())
                .username(retrivedUser.getUsername())
                .createdAt(retrivedUser.getCreatedAt())
                .dateOfBirth(retrivedUser.getDateOfBirth())
                .phoneNumber(retrivedUser.getPhoneNumber())
                .build();

        return res;
    }

    /**
     * for user to update their profiles but only general data
     * @param id
     * @param requestDTO
     * @return
     */
    @Override
    public UserResponseDTO updateUserInfo(Long id, UpdateUserRequestDTO requestDTO) {

        if (!checkAuthorization(id)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }

        String firstname_smooth = smooth(requestDTO.getFirstName());
        String lastname_smooth = smooth(requestDTO.getLastName());
        User user = getUserById(id);

        if (requestDTO.getFirstName() != null) {
            user.setFirstName(firstname_smooth);
        }
        if (requestDTO.getLastName() != null) {
            user.setLastName(lastname_smooth);
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getGender() != null) {
            user.setGender(Gender.fromString(requestDTO.getGender()));
        }
        if (requestDTO.getDateOfBirth() != null) {
            user.setDateOfBirth(requestDTO.getDateOfBirth());
        }
        if (requestDTO.getPhone() != null) {
            user.setPhoneNumber(requestDTO.getPhone());
        }

        System.out.println(user.toString());

        User newUser = userRepository.save(user);

        UserResponseDTO res = UserResponseDTO.builder()
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .gender(newUser.getGender())
                .dateOfBirth(newUser.getDateOfBirth())
                .phoneNumber(newUser.getPhoneNumber())
                .build();
        return res;
    }

    /**
     * De-activate and Activate user account
     * Basically banning user account but not deleting them
     * @param username
     * @return
     */
    @Override
    public AccountStatus changeAccountStatus(String username) {
        User user = userRepository.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        user.setIsActive(!user.getIsActive());

        User newUser = userRepository.save(user);


        return newUser.getIsActive() ? AccountStatus.ACTIVE : AccountStatus.INACTIVE;
    }

    /**
     * To assure user A can't access user B's data
     * @param userId
     * @return
     */
    private boolean checkAuthorization(Long userId) {
        String username = getUserById(userId).getUsername();
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }


    private String smooth(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }


}
