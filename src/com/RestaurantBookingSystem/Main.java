package com.RestaurantBookingSystem;

import com.RestaurantBookingSystem.enums.*;
import com.RestaurantBookingSystem.exception.BookingException;
import com.RestaurantBookingSystem.exception.ErrorCode;
import com.RestaurantBookingSystem.model.*;
import com.RestaurantBookingSystem.search.impl.*;
import com.RestaurantBookingSystem.service.impl.*;
import com.RestaurantBookingSystem.service.interfaces.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int passed = 0, failed = 0;

    static void assertTrue(boolean condition, String testName) {
        if (condition) {
            System.out.println("  PASS: " + testName);
            passed++;
        } else {
            System.out.println("  FAIL: " + testName);
            failed++;
        }
    }

    public static void main(String[] args) throws Exception {
        RestaurantService restaurantService = new RestaurantServiceImpl();
        SearchService searchService = new SearchServiceImpl(restaurantService);
        BookingService bookingService = new BookingServiceImpl(restaurantService, 7);

        testRegisterRestaurant(restaurantService);
        testUpdateTimeSlots(restaurantService);
        testSearchRestaurant(searchService);
        testBookTable(bookingService);
        testCancelBooking(bookingService);
        testErrorScenarios(restaurantService, bookingService);
        testConcurrentBookings(restaurantService, bookingService);

        System.out.println("\n========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");
    }

    static void testRegisterRestaurant(RestaurantService svc) {
        System.out.println("\n--- Register Restaurant ---");

        Restaurant r1 = createRestaurant("r1", "Taj Palace", "Mumbai", "Bandra",
                Cuisine.INDIAN, FoodType.BOTH, 2000, 3, 4);
        svc.registerRestaurant(r1);
        assertTrue(svc.getRestaurant("r1").getName().equals("Taj Palace"), "Register and fetch restaurant");

        Restaurant r2 = createRestaurant("r2", "Dragon Wok", "Mumbai", "Andheri",
                Cuisine.CHINESE, FoodType.NON_VEG, 1200, 2, 4);
        svc.registerRestaurant(r2);

        Restaurant r3 = createRestaurant("r3", "Pizza Italia", "Delhi", "Connaught Place",
                Cuisine.ITALIAN, FoodType.VEG, 800, 2, 2);
        svc.registerRestaurant(r3);

        Restaurant r4 = createRestaurant("r4", "Spice Garden", "Mumbai", "Bandra",
                Cuisine.INDIAN, FoodType.VEG, 600, 2, 4);
        svc.registerRestaurant(r4);

        assertTrue(svc.getAllRestaurants().size() == 4, "Total 4 restaurants registered");

        try {
            svc.registerRestaurant(createRestaurant("r1", "Duplicate", "X", "Y",
                    Cuisine.INDIAN, FoodType.VEG, 100, 1, 2));
            assertTrue(false, "Duplicate registration should throw");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.DUPLICATE_RESTAURANT, "Duplicate restaurant rejected");
        }
    }

    static void testUpdateTimeSlots(RestaurantService svc) {
        System.out.println("\n--- Update Time Slots ---");

        List<LocalTime> newSlots = Arrays.asList(
                LocalTime.of(18, 0), LocalTime.of(19, 0), LocalTime.of(20, 0));
        svc.updateTimeSlots("r1", newSlots);

        Restaurant r = svc.getRestaurant("r1");
        assertTrue(r.getAvailableSlotStartTimes().size() == 3, "Slot times updated to 3");
        assertTrue(r.getAvailableSlotStartTimes().contains(LocalTime.of(18, 0)), "Contains 18:00 slot");
    }

    static void testSearchRestaurant(SearchService svc) {
        System.out.println("\n--- Search Restaurant ---");

        List<Restaurant> byCity = svc.search(new CitySearchStrategy(), "Mumbai");
        assertTrue(byCity.size() == 3, "Search by city Mumbai returns 3");

        List<Restaurant> byArea = svc.search(new AreaSearchStrategy(), "Bandra");
        assertTrue(byArea.size() == 2, "Search by area Bandra returns 2");

        List<Restaurant> byCuisine = svc.search(new CuisineSearchStrategy(), Cuisine.INDIAN);
        assertTrue(byCuisine.size() == 2, "Search by Indian cuisine returns 2");

        List<Restaurant> byName = svc.search(new NameSearchStrategy(), "pizza");
        assertTrue(byName.size() == 1 && byName.get(0).getName().equals("Pizza Italia"),
                "Search by name 'pizza' finds Pizza Italia");

        List<Restaurant> byCost = svc.search(new CostForTwoSearchStrategy(), 1000.0);
        assertTrue(byCost.size() == 2, "Search by cost <= 1000 returns 2");

        List<Restaurant> byFoodType = svc.search(new FoodTypeSearchStrategy(), FoodType.VEG);
        assertTrue(byFoodType.size() == 3, "Search VEG returns 3 (includes BOTH)");

        List<Restaurant> byNonVeg = svc.search(new FoodTypeSearchStrategy(), FoodType.NON_VEG);
        assertTrue(byNonVeg.size() == 2, "Search NON_VEG returns 2 (includes BOTH)");
    }

    static void testBookTable(BookingService svc) {
        System.out.println("\n--- Book Table ---");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Booking b1 = svc.bookTable("user1", "r1", 2, tomorrow, LocalTime.of(19, 0));
        assertTrue(b1 != null && b1.getStatus() == BookingStatus.CONFIRMED, "Book table for 2 people");

        Booking b2 = svc.bookTable("user2", "r1", 3, tomorrow, LocalTime.of(19, 0));
        assertTrue(b2 != null && !b1.getTable().getTableId().equals(b2.getTable().getTableId()),
                "Second booking gets different table for same slot");
    }

    static void testCancelBooking(BookingService svc) {
        System.out.println("\n--- Cancel Booking ---");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Booking b = svc.bookTable("user3", "r3", 2, tomorrow, LocalTime.of(12, 0));
        String bookingId = b.getBookingId();
        svc.cancelBooking(bookingId);
        assertTrue(svc.getBooking(bookingId).getStatus() == BookingStatus.CANCELLED,
                "Booking cancelled successfully");

        Booking rebooking = svc.bookTable("user4", "r3", 2, tomorrow, LocalTime.of(12, 0));
        assertTrue(rebooking.getTable().getTableId().equals(b.getTable().getTableId()),
                "Cancelled slot re-bookable, same table assigned");
    }

    static void testErrorScenarios(RestaurantService rSvc, BookingService bSvc) {
        System.out.println("\n--- Error Scenarios ---");

        try {
            rSvc.getRestaurant("nonexistent");
            assertTrue(false, "Should throw for nonexistent restaurant");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.RESTAURANT_NOT_FOUND, "Restaurant not found error");
        }

        try {
            bSvc.bookTable("user", "r1", 0, LocalDate.now().plusDays(1), LocalTime.of(12, 0));
            assertTrue(false, "Should throw for 0 people");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.INVALID_PEOPLE_COUNT, "Invalid people count error");
        }

        try {
            bSvc.bookTable("user", "r1", 2, LocalDate.now().plusDays(30), LocalTime.of(12, 0));
            assertTrue(false, "Should throw for date too far");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.BOOKING_DATE_OUT_OF_RANGE, "Date out of range error");
        }

        try {
            bSvc.bookTable("user", "r1", 2, LocalDate.now().plusDays(1), LocalTime.of(5, 0));
            assertTrue(false, "Should throw for invalid slot");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.INVALID_SLOT, "Invalid slot error");
        }

        try {
            bSvc.getBooking("fake-id");
            assertTrue(false, "Should throw for nonexistent booking");
        } catch (BookingException e) {
            assertTrue(e.getErrorCode() == ErrorCode.BOOKING_NOT_FOUND, "Booking not found error");
        }
    }

    static void testConcurrentBookings(RestaurantService rSvc, BookingService bSvc) throws Exception {
        System.out.println("\n--- Concurrent Bookings ---");

        Restaurant concurrentRestaurant = createRestaurant("rc", "Concurrency Cafe",
                "Bangalore", "Koramangala", Cuisine.CONTINENTAL, FoodType.BOTH, 1500, 2, 4);
        rSvc.registerRestaurant(concurrentRestaurant);

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime slot = LocalTime.of(12, 0);
        int numThreads = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final String userId = "concUser" + i;
            futures.add(executor.submit(() -> {
                latch.await();
                try {
                    Booking b = bSvc.bookTable(userId, "rc", 2, tomorrow, slot);
                    successCount.incrementAndGet();
                    return "SUCCESS: " + b.getBookingId() + " -> " + b.getTable().getTableId();
                } catch (BookingException e) {
                    failCount.incrementAndGet();
                    return "REJECTED: " + e.getMessage();
                }
            }));
        }

        latch.countDown();

        for (Future<String> f : futures) {
            System.out.println("    " + f.get());
        }
        executor.shutdown();

        assertTrue(successCount.get() == 2,
                "Exactly 2 bookings succeed (2 tables available), got " + successCount.get());
        assertTrue(failCount.get() == 8,
                "Exactly 8 bookings rejected, got " + failCount.get());
    }

    static Restaurant createRestaurant(String id, String name, String city, String area,
                                        Cuisine cuisine, FoodType foodType, double cost,
                                        int tableCount, int tableCapacity) {
        List<Table> tables = new ArrayList<>();
        for (int i = 1; i <= tableCount; i++) {
            tables.add(new Table(id + "-T" + i, tableCapacity));
        }
        List<LocalTime> slots = Arrays.asList(
                LocalTime.of(12, 0), LocalTime.of(13, 0),
                LocalTime.of(19, 0), LocalTime.of(20, 0));
        return new Restaurant(id, name, city, area, cuisine, foodType, cost, tables, slots);
    }
}
