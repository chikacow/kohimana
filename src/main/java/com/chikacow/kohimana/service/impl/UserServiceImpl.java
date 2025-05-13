package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.SearchRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
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
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }

    @Override
    public User getByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }



    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {


        //on next version

        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }



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

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumnsAndSearch(int pageNo, int pageSize, String sortBy, String search) {
        return searchRepository.getAllUsersWithSortByMultipleColumnsAndSearch(pageNo, pageSize, sortBy, search);
    }


    @Override
    public UserResponseDTO getUserInfo(String username) {
        if (!checkAuthorization(username)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }


        User retrivedUser =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
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

    @Override
    public UserResponseDTO updateUserInfo(String username, UpdateUserRequestDTO requestDTO) {

        if (!checkAuthorization(username)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        if (requestDTO.getFirstName() != null) {
            user.setFirstName(requestDTO.getFirstName());
        }
        if (requestDTO.getLastName() != null) {
            user.setLastName(requestDTO.getLastName());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getGender() != null) {
            user.setGender(requestDTO.getGender());
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

    @Override
    public AccountStatus changeAccountStatus(String username) {
        User user = userRepository.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));

        user.setIsActive(!user.getIsActive());

        User newUser = userRepository.save(user);


        return newUser.getIsActive() ? AccountStatus.ACTIVE : AccountStatus.INACTIVE;
    }

    private boolean checkAuthorization(String username) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }
}
