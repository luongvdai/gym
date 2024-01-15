package fpt.gymmanagement.utils;

import com.querydsl.jpa.impl.JPAQuery;
import fpt.gymmanagement.entity.Order;
import fpt.gymmanagement.entity.OrderDetail;
import fpt.gymmanagement.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CommonAppUtils {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static Map<String, String> buildRoleClaims(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("role", user.getRole().getPosition());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        if(user.getBranchId() == null) {
            claims.put("branchId", "NOT_YET");
        } else {
            claims.put("branchId", user.getBranchId());
        }
        claims.put("type", user.getType().toString());
        claims.put("isVerify", user.getIsVerify().toString());
        return claims;
    }

    public static String getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

    public static boolean isMentor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("MENTOR"));
    }

    public static boolean isManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("MANAGER"));
    }

    public static boolean isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("USER"));
    }

    public static void copyPropertiesNotNull(Object source, Object target) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        String[] nullNames = Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, nullNames);
    }

    public static boolean isUndefinedString(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }

    public static Page<?> getDataPage(JPAQuery<?> query, Integer page, Integer size) {
        long count = query.stream().count();
        query.offset((long) page * size).limit(size);
        var data = query.fetch();
        return new PageImpl<>(data, PageRequest.of(page, size), count);
    }

    public static String randomText() {
        SecureRandom random = new SecureRandom();
        StringBuilder value = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(ALPHABET.length());
            value.append(ALPHABET.charAt(randomIndex));
        }
        return value.toString();
    }

    public static String generateOtp() {
        // Generate a random OTP of specified length
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public static Map<String, Long> getFromDateAndToDateCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month - 1, 1, 0, 0, 0); // Đặt ngày đầu tiên của tháng
        long fromDate = calendar.getTime().getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long toDate = calendar.getTime().getTime();
        Map<String, Long> result = new HashMap<>();
        result.put("fromDate", fromDate);
        result.put("toDate", toDate);
        return result;
    }

    public static Map<String, FilterTimeStats> getStatsEveryMonthByYear(int year) {
        Map<String, FilterTimeStats> response = new LinkedHashMap<>();
        int start = 1;
        for (Month month : Month.values()) {
            LocalDate fromDate = LocalDate.of(year, month, 1);//ngay mong 1
            LocalDate toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());//cuoi thang
            Map<Long, Long> fromTo = new HashMap<>();
            FilterTimeStats filterTimeStats = new FilterTimeStats();
            filterTimeStats.setFromDate(getTimestampFromDate(fromDate));
            filterTimeStats.setToDate(getTimestampFromDate(toDate));
            response.put(start + "", filterTimeStats);
            start++;
        }
        return response;
    }

    public static long getTimestampFromDate(LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static double getTotalMoneyByOrders(List<Order> orders) {
        double result = 0.0;
        for (Order order : orders) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail od : orderDetails) {
                result += od.getPrice();
            }
        }
        return result;
    }

    public static int getCurrentYear() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    @Getter
    @Setter
    public static class FilterTimeStats {
        Long fromDate;
        Long toDate;
    }
}
