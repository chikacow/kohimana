package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.UpdateUserRequestDTO;
import com.chikacow.kohimana.dto.request.UserRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.StatisticalResponse;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.InvalidDataException;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.UserMapper;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.SearchRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.AccountStatus;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.helper.SmoothData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.chikacow.kohimana.util.AppConst.SORT_BY;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SearchRepository searchRepository;

    @PersistenceContext
    private final EntityManager entityManager;

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
                            .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
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
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Find User object by their email, only one record returned as the requirements
     * @return User object
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    /**
     * Find User object by their username, only one record returned as the requirements
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
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
        int realPageNo = pageNo > 0 ? pageNo - 1 : 0;

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
        List<UserResponseDTO> resList = page.stream().map(UserMapper::fromEntityToResponseDTO).toList();

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
        int realPageNo = pageNo > 0 ? pageNo - 1 : 0;

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
        List<UserResponseDTO> resList = page.stream().map(UserMapper::fromEntityToResponseDTO).toList();

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

     * @param search
     * @return
     */
    @Override
    public PageResponse<?> getAllUsersWithSearch(int pageNo, int pageSize, String search) {
        return searchRepository.getAllUsersWithSearch(pageNo, pageSize, search);
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
        checkAuthorization(id);
        return UserMapper.fromEntityToResponseDTO(getUserById(id));
    }

    /**
     * for user to update their profiles but only general data
     * @param id
     * @param requestDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO updateUserInfo(Long id, UpdateUserRequestDTO requestDTO) {
        checkAuthorization(id);

        User user = getUserById(id);
        UserMapper.updateUserFromRequestDTO(user, requestDTO);

        return UserMapper.fromEntityToResponseDTO(user);
    }

    /**
     * De-activate and Activate user account
     * Basically banning user account but not deleting them
     * @param id
     * @return
     */
    @Override
    public AccountStatus changeAccountStatus(Long id) {
        User user = getUserById(id);

        user.setIsActive(!user.getIsActive());

        User newUser = userRepository.save(user);

        return newUser.getIsActive() ? AccountStatus.ACTIVE : AccountStatus.INACTIVE;
    }

    @Override
    public List<UserResponseDTO> getActiveUsers() {
        List<User> allUsers = userRepository.findAll();
        List<User> activeUsers = allUsers.stream().filter(User::getIsActive).toList();

        return activeUsers.stream()
                .map(UserMapper::fromEntityToResponseDTO)
                .toList();

    }

    @Override
    public Set<String> getDuplicateEmails() {
        List<User> allUsers = userRepository.findAll();
        Set<String> distinctEmail = allUsers.stream().map(u -> u.getEmail()).collect(Collectors.toSet());

        Map<String, Integer> mp = distinctEmail.stream().collect(Collectors.toMap(Function.identity(), email -> 0));

        allUsers.stream().forEach(e -> mp.put(e.getEmail(), mp.get(e.getEmail()) + 1));

        Set<String> res = mp.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return res;
    }

    @Override
    public StatisticalResponse.AgeAndGender getAgeAndGenderStats() {
        List<User> allUsers = userRepository.findAll();

        StatisticalResponse.AgeAndGender res = StatisticalResponse.AgeAndGender.builder()
                .womenNumUser(
                        allUsers.stream()
                                .filter(u -> u.getGender() == Gender.FEMALE)
                                .count()
                )
                .menNumUser(
                        allUsers.stream()
                                .filter(u -> u.getGender() == Gender.MALE)
                                .count()
                )
                .womenAverageAge(
                        allUsers.stream()
                                .filter(u -> u.getGender() == Gender.FEMALE && u.getDateOfBirth() != null)
                                .mapToInt(u -> LocalDate.now().getYear() - u.getDateOfBirth().getYear())
                                .average().orElse(0.0)
                )
                .menAverageAge(
                        allUsers.stream()
                                .filter(u -> u.getGender() == Gender.MALE && u.getDateOfBirth() != null)
                                .mapToInt(u -> LocalDate.now().getYear() - u.getDateOfBirth().getYear())
                                .average().orElse(0.0)
                )
                .build();

        return res;
    }

    @Override
    public List<String> topOrderedUsers(int topNum) {
        List<User> allUsers = userRepository.findAll();
        List<User> sortedUsersDesc = allUsers.stream()
                .sorted(Comparator.comparingInt((User u) -> u.getOrders().size()).reversed())
                .limit(topNum).toList();

        List<String> res = sortedUsersDesc.stream().map(u -> u.getUsername()).toList();

        return res;

    }

    @Override
    public Map<String, String> getAllUsernameAndPassword() {
        List<User> allUsers = userRepository.findAll();
        Map<String, String> res = allUsers.stream().collect(Collectors.toMap(User::getUsername, User::getPassword));

        return res;
    }

    @Override
    public boolean isUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new InvalidDataException("Username already exists");
        }
        return true;
    }

    /**
     * To assure user A can't access user B's data
     * @param userId
     * @return
     */
    private boolean checkAuthorization(Long userId) {
        String username = getUserById(userId).getUsername();
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            throw new HaveNoAccessToResourceException("Do not access other user's info");
        }
        return true;

    }





}
