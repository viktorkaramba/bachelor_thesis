package com.unicyb.minitaxi.database;

import com.unicyb.minitaxi.services.DateService;

public class SQLQuery {
    public static final String INSERT_CAR="INSERT INTO CARS VALUES(car_sequence.nextval, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_CAR="SELECT * FROM CARS";
    public static final String SELECT_ALL_CAR_BY_CAR_CLASS="SELECT * FROM CARS WHERE CC_ID=?";

    public static final String SELECT_ALL_CAR_BY_ID="SELECT * FROM CARS WHERE CARS_ID=?";
    public static final String UPDATE_CAR="UPDATE CARS SET BRAND=?, PRODUCER=?, NUMBER_OF_SEATS=?," +
            "CC_ID=?, IN_USE=?, IN_ORDER=? WHERE CARS_ID=?";
    public static final String DELETE_CAR="DELETE FROM CARS WHERE CARS_ID=?";
    public static final String INSERT_DRIVER="INSERT INTO DRIVERS(drivers_id, fullname_id, telephone_number, experience, " +
            "salary, date_d) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_DRIVER="SELECT * FROM DRIVERS";
    public static final String SELECT_ALL_DRIVER_BY_ID="SELECT * FROM DRIVERS WHERE DRIVERS_ID=?";
    public static final String UPDATE_DRIVER="UPDATE DRIVERS SET CARS_ID=?, FULLNAME_ID=?, TELEPHONE_NUMBER=?," +
            "EXPERIENCE=?, SALARY=? WHERE DRIVERS_ID=?";
    public static final String DELETE_DRIVER="DELETE FROM DRIVERS WHERE DRIVERS_ID=?";
    public static final String INSERT_FULL_NAME="INSERT INTO FULLNAME VALUES(fullname_sequence.nextval, ?, ?, ?)";
    public static final String SELECT_ALL_FULL_NAME="SELECT * FROM FULLNAME";
    public static final String SELECT_ID_BY_FULLNAME="SELECT FULLNAME_ID FROM FULLNAME WHERE FIRSTNAME=? AND SURNAME=? " +
            "AND PATRONYMIC=?";
    public static final String SELECT_ALL_FULL_NAME_BY_ID="SELECT * FROM FULLNAME WHERE FULLNAME_ID=?";
    public static final String UPDATE_FULL_NAME="UPDATE FULLNAME SET FIRSTNAME=?, SURNAME=?, PATRONYMIC=? WHERE FULLNAME_ID=?";
    public static final String DELETE_FULL_NAME="DELETE FROM FULLNAME WHERE FULLNAME_ID=?";
    public static final String SELECT_CARS_DRIVERS_PRICE_FOR_USER_MENU="SELECT DRIVERS.DRIVERS_ID, CARS.PRODUCER, CARS.BRAND, " +
            "FULLNAME.FIRSTNAME, FULLNAME.SURNAME, PRICE_PER_KILOMETER_BY_TARIFF." +
            DateService.recognize(java.time.LocalTime.now()) + " FROM CARS JOIN DRIVERS " +
            "ON CARS.CARS_ID=DRIVERS.CARS_ID JOIN PRICE_PER_KILOMETER_BY_TARIFF \n" +
            "ON PRICE_PER_KILOMETER_BY_TARIFF.CARS_ID = CARS.CARS_ID\n" +
            "JOIN FULLNAME ON FULLNAME.FULLNAME_ID=DRIVERS.FULLNAME_ID WHERE CARS.IN_USE='YES' AND CARS.IN_ORDER='NO'";
    public static final String SELECT_DRIVER_PROFILE_INFO="SELECT DRIVERS.DRIVERS_ID, FULLNAME.FIRSTNAME, " +
            "FULLNAME.SURNAME, FULLNAME.PATRONYMIC,\n" +
            "CARS.PRODUCER, CARS.BRAND, DRIVERS.EXPERIENCE, DRIVERS.SALARY\n" +
            "FROM DRIVERS \n" +
            "JOIN CARS \n" +
            "ON CARS.CARS_ID=DRIVERS.CARS_ID \n" +
            "JOIN FULLNAME \n" +
            "ON FULLNAME.FULLNAME_ID=DRIVERS.FULLNAME_ID\n" +
            "WHERE DRIVERS.DRIVERS_ID=?";
    public static final String INSERT_USER_WITH_ID="INSERT INTO USERS VALUES(?, ?, ?, ?, ?)";
    public static final String INSERT_USER="INSERT INTO USERS VALUES(users_sequence.nextval, ?, ?, ?, ?)";
    public static final String SELECT_ALL_USER="SELECT * FROM USERS";
    public static final String SELECT_USER_BY_USERNAME="SELECT * FROM USERS WHERE USERNAME=?";
    public static final String UPDATE_USER="UPDATE USERS SET USERNAME=?, PASSWORD=?, ROLE=? RANKS_ID=?, WHERE USERS_ID=?";
    public static final String DELETE_USER="DELETE FROM USERS WHERE USERS_ID=?";
    public static final String INSERT_ORDER="INSERT INTO ORDERS VALUES(order_sequence.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_ORDERS="SELECT * FROM ORDERS";
    public static final String SELECT_ALL_ORDERS_BY_DRIVER_ID="SELECT DATE_O, ADDRESS_CUSTOMER, ADDRESS_DELIVERY, " +
            "TELEPHONE_CUSTOMER, PRICE, RATING, NUMBER_OF_KILOMETERS, CUSTOMER_NAME, USERS_ID, USER_COMMENT FROM ORDERS WHERE DRIVERS_ID=?";
    public static final String REPORT_ORDERS="SELECT * FROM ORDERS " +
            "WHERE TRUNC(DATE_O) >= TRUNC(CAST(? AS TIMESTAMP)) AND TRUNC(DATE_O) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_BY_ID_ORDERS="SELECT * FROM ORDERS " +
            "WHERE TRUNC(DATE_O) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND  TRUNC(DATE_O) <= TRUNC(CAST(? AS TIMESTAMP)) AND DRIVERS_ID=?";
    public static final String INSERT_CAR_CLASSES="INSERT INTO CARCLASSES VALUES(CARCLASSES_SEQUENCE.nextval, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_CAR_CLASSES="SELECT * FROM CARCLASSES";
    public static final String SELECT_ALL_CAR_CLASS_BY_ID="SELECT * FROM CARCLASSES WHERE CC_ID=?";
    public static final String UPDATE_CAR_CLASSES="UPDATE CARCLASSES SET NAME=?, MIN_EXPERIENCE=?, MIN_KILOMETERS=?," +
            "PRICE=?, MIN_RATING=? WHERE CC_ID=?";
    public static final String DELETE_CAR_CLASSES="DELETE FROM CARCLASSES WHERE CC_ID=?";
    public static final String INSERT_MAINTENANCE_COSTS_CARS="INSERT INTO MAINTENANCE_COSTS_CARS VALUES(" +
            "MAINTENANCE_COSTS_CARS_SEQUENCE.nextval, ?, ?, ?, ?)";
    public static final String SELECT_ALL_MAINTENANCE_COSTS_CARS="SELECT * FROM MAINTENANCE_COSTS_CARS";
    public static final String UPDATE_MAINTENANCE_COSTS_CARS="UPDATE MAINTENANCE_COSTS_CARS SET DATE_MCC=?, TYPE=?, COSTS=?," +
            "CARS_ID=? WHERE MCC_ID=?";
    public static final String DELETE_MAINTENANCE_COSTS_CARS="DELETE FROM MAINTENANCE_COSTS_CARS WHERE MCC_ID=?";
    public static final String INSERT_PRICE_PER_KILOMETER_BY_TARIFF="INSERT INTO PRICE_PER_KILOMETER_BY_TARIFF VALUES(" +
            "PRICE_PER_KILOMETERS_BY_TARIFF_SEQUENCE.nextval, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_PRICE_PER_KILOMETER_BY_TARIFF="SELECT * FROM PRICE_PER_KILOMETER_BY_TARIFF";
    public static final String UPDATE_PRICE_PER_KILOMETER_BY_TARIFF="UPDATE PRICE_PER_KILOMETER_BY_TARIFF SET CARS_ID=?, " +
            "MORNING=?, DAY=?, EVENING=?, NIGHT=? WHERE PPKBT_ID=?";
    public static final String DELETE_PRICE_PER_KILOMETER_BY_TARIFF="DELETE FROM PRICE_PER_KILOMETER_BY_TARIFF WHERE PPKBT_ID=?";
    public static final String SELECT_ALL_ADDRESSES="SELECT * FROM ADDRESSES";
    public static final String SELECT_ALL_DRIVER_RATING="SELECT * FROM DRIVER_RATING";
    public static final String SELECT_ALL_DRIVER_RATING_BY_DRIVER_ID="SELECT * FROM DRIVER_RATING WHERE DRIVERS_ID=?";
    public static final String REPORT_DRIVER_RATING="SELECT * FROM DRIVER_RATING " +
            "WHERE TRUNC(DATE_DR) >= TRUNC(CAST(? AS TIMESTAMP))" +
            "AND TRUNC(DATE_DR) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_DRIVER_RATING_BY_DRIVER_ID="SELECT * FROM DRIVER_RATING " +
            "WHERE TRUNC(DATE_DR) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_DR) <= TRUNC(CAST(? AS TIMESTAMP)) AND DRIVERS_ID=?";
    public static final String INSERT_DRIVER_CAR_RECOMMENDATIONS="INSERT INTO DRIVERS_CAR_RECOMMENDATIONS VALUES(" +
            "DRIVER_CAR_REC_SEQUENCE.nextval, ?, ?, ?, ?)";
    public static final String SELECT_ALL_DRIVER_CAR_REC="SELECT * FROM DRIVERS_CARS_RECOMMENDATIONS";
    public static final String REPORT_DRIVER_CAR_REC="SELECT * FROM DRIVERS_CARS_RECOMMENDATIONS " +
            "WHERE TRUNC(DATE_DCR) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_DCR) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_BY_ID_DRIVER_CAR_REC="SELECT * FROM DRIVERS_CARS_RECOMMENDATIONS " +
            "WHERE TRUNC(DATE_DCR) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_DCR) <= TRUNC(CAST(? AS TIMESTAMP)) AND DRIVERS_ID=?";
    public static final String SELECT_ALL_DRIVER_CAR_REC_BY_DRIVER_ID="SELECT * FROM DRIVERS_CARS_RECOMMENDATIONS WHERE DRIVERS_ID=? AND STATUS='WAITING'";
    public static final String UPDATE_DRIVER_CAR_REC="UPDATE DRIVERS_CARS_RECOMMENDATIONS SET DATE_DCR=?, DRIVERS_ID=?, CARS_ID=?, STATUS=? WHERE DCR_ID=?";
    public static final String SELECT_ALL_INCOME_CARS="SELECT * FROM INCOME_CARS";
    public static final String REPORT_INCOME_CARS="SELECT * FROM INCOME_CARS " +
            "WHERE TRUNC(DATE_IC) >= TRUNC(CAST(? AS TIMESTAMP))" +
            "AND TRUNC(DATE_IC) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_BY_ID_INCOME_CARS="SELECT * FROM INCOME_CARS " +
            "WHERE TRUNC(DATE_IC) >= TRUNC(CAST(? AS TIMESTAMP))" +
            "AND TRUNC(DATE_IC) <= TRUNC(CAST(? AS TIMESTAMP)) AND CARS_ID=?";
    public static final String SELECT_ALL_NUMBER_OF_KILOMETERS="SELECT * FROM NUMBER_OF_KILOMETERS";
    public static final String REPORT_NUMBER_OF_KILOMETERS="SELECT * FROM NUMBER_OF_KILOMETERS " +
            "WHERE TRUNC(DATE_NOK) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_NOK) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_BY_ID_NUMBER_OF_KILOMETERS="SELECT * FROM NUMBER_OF_KILOMETERS " +
            "WHERE TRUNC(DATE_NOK) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_NOK) <= TRUNC(CAST(? AS TIMESTAMP)) AND DRIVERS_ID=?";
    public static final String SELECT_ALL_NUMBER_OF_KILOMETERS_BY_DRIVER_ID="SELECT * FROM NUMBER_OF_KILOMETERS WHERE DRIVERS_ID=?";
    public static final String SELECT_ALL_WORKLOAD_DRIVERS="SELECT * FROM WORKLOAD_DRIVERS";
    public static final String REPORT_WORKLOAD_DRIVERS="SELECT * FROM WORKLOAD_DRIVERS " +
            "WHERE TRUNC(DATE_WLD) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_WLD) <= TRUNC(CAST(? AS TIMESTAMP))";
    public static final String REPORT_BY_ID_WORKLOAD_DRIVERS="SELECT * FROM WORKLOAD_DRIVERS " +
            "WHERE TRUNC(DATE_WLD) >= TRUNC(CAST(? AS TIMESTAMP)) " +
            "AND TRUNC(DATE_WLD) <= TRUNC(CAST(? AS TIMESTAMP)) AND DRIVERS_ID=?";
    public static final String INSERT_DRIVER_LOCATION="INSERT INTO DRIVERS_LOCATION VALUES(" +
            "DRIVER_LOCATION_SEQUENCE.nextval, ?, ?, ?)";
    public static final String SELECT_ALL_DRIVER_LOCATION="SELECT * FROM DRIVER_LOCATION";
    public static final String UPDATE_DRIVER_LOCATION="UPDATE DRIVER_LOCATION SET DRIVERS_ID=?, LATITUDE=?, LONGITUDE=?" +
            "WHERE D_L_ID = ?";
    public static final String DELETE_DRIVER_LOCATION="DELETE FROM DRIVER_LOCATION WHERE D_L_ID=?";
    public static final String SELECT_USER_ORDERS_INFO="SELECT FULLNAME.FIRSTNAME, FULLNAME.SURNAME, CARS.PRODUCER, CARS.BRAND, \n" +
            "ORDERS.ADDRESS_CUSTOMER, ORDERS.ADDRESS_DELIVERY, ORDERS.RATING, ORDERS.PRICE,\n" +
            "ORDERS.DATE_O\n" +
            "FROM ORDERS\n" +
            "JOIN DRIVERS\n" +
            "ON DRIVERS.DRIVERS_ID = ORDERS.DRIVERS_ID\n" +
            "JOIN CARS\n" +
            "ON CARS.CARS_ID = DRIVERS.CARS_ID\n" +
            "JOIN FULLNAME\n" +
            "ON DRIVERS.FULLNAME_ID = FULLNAME.FULLNAME_ID\n" +
            "WHERE ORDERS.USERS_ID = ?";
    public static final String SELECT_USER_STATS="SELECT ORDERS_ID, USER_COMMENT, USERS.RANKS_ID \n" +
            "FROM ORDERS \n" +
            "JOIN USERS\n" +
            "ON USERS.USERS_ID=ORDERS.USERS_ID \n" +
            "WHERE ORDERS.USERS_ID=?";
    public static final String INSERT_FAVOURITE_ADDRESSES="INSERT INTO FAVOURITE_ADDRESSES VALUES(" +
            "FAVOURITE_ADDRESSES_SEQUENCE.nextval, ?, ?";
    public static final String SELECT_FAVOURITE_ADDRESSES_BY_USER="SELECT * FROM FAVOURITE_ADDRESSES WHERE USERS_ID=?";
    public static final String UPDATE_FAVOURITE_ADDRESSES="UPDATE FROM FAVOURITE_ADDRESSES SET USERS_ID=?, " +
            "ADDRESS = ? WHERE F_A_ID = ?";
    public static final String DELETE_FAVOURITE_ADDRESS="DELETE FROM FAVOURITE_ADDRESSES WHERE F_A_ID = ?";
    public static final String DELETE_FAVOURITE_ADDRESS_BY_USER_ID_ADDRESS="DELETE FROM FAVOURITE_ADDRESSES WHERE USERS_ID = ? " +
            "AND ADDRESS = ?";
    public static final String INSERT_FAVOURITE_DRIVERS="INSERT INTO FAVOURITE_DRIVERS VALUES(" +
            "FAVOURITE_DRIVERS_SEQUENCE.nextval, ?, ?";
    public static final String SELECT_FAVOURITE_DRIVERS_BY_USER="SELECT * FROM FAVOURITE_DRIVERS WHERE USERS_ID=?";
    public static final String UPDATE_FAVOURITE_DRIVERS="UPDATE FROM FAVOURITE_DRIVERS SET USERS_ID=?, " +
            "ADDRESS = ? WHERE F_D_ID = ?";
    public static final String DELETE_FAVOURITE_DRIVERS="DELETE FROM FAVOURITE_DRIVERS WHERE F_D_ID = ?";
    public static final String DELETE_FAVOURITE_DRIVERS_BY_DRIVER_ID_USER_ID ="DELETE FROM FAVOURITE_DRIVERS WHERE DRIVERS_ID = ? " +
            "AND USERS_ID=?";
    public static final String SELECT_FAVOURITE_DRIVERS_INFO_BY_USER_ID_AND_DRIVER_ID="SELECT " +
            "favourite_drivers.DRIVERS_ID, COUNT(ORDERS.RATING), AVG(ORDERS.RATING), FULLNAME.FIRSTNAME, " +
            "FULLNAME.SURNAME, CARS.PRODUCER, CARS.BRAND\n" +
            "FROM favourite_drivers \n" +
            "JOIN DRIVERS\n" +
            "ON DRIVERS.DRIVERS_ID = favourite_drivers.DRIVERS_ID\n" +
            "JOIN ORDERS\n" +
            "ON ORDERS.DRIVERS_ID = favourite_drivers.DRIVERS_ID AND ORDERS.USERS_ID = favourite_drivers.USERS_ID\n" +
            "JOIN FULLNAME\n" +
            "ON FULLNAME.FULLNAME_ID=DRIVERS.FULLNAME_ID\n" +
            "JOIN CARS\n" +
            "ON DRIVERS.CARS_ID = CARS.CARS_ID\n" +
            "WHERE favourite_drivers.USERS_ID = ?\n" +
            "GROUP BY favourite_drivers.DRIVERS_ID, FULLNAME.FIRSTNAME, FULLNAME.SURNAME, CARS.PRODUCER, CARS.BRAND";

    public static final String SELECT_FAVOURITE_ADDRESSES_INFO_BY_USER_ID = "SELECT " +
            "favourite_addresses.USERS_ID,  " +
            "favourite_addresses.ADDRESS, COUNT(ORDERS.ORDERS_ID)\n" +
            "FROM favourite_addresses \n" +
            "JOIN ORDERS\n" +
            "ON ORDERS.USERS_ID = favourite_addresses.USERS_ID AND\n" +
            "(ORDERS.address_customer = favourite_addresses.ADDRESS OR ORDERS.address_delivery = favourite_addresses.ADDRESS)\n" +
            "WHERE favourite_addresses.USERS_ID = ?\n" +
            "GROUP BY favourite_addresses.USERS_ID,  favourite_addresses.ADDRESS";
    public static final String INSERT_RANK="INSERT INTO RANKS VALUES(RANK_SEQUENCE.nextval, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_RANK="SELECT * FROM RANKS";
    public static final String SELECT_RANK_BY_ID="SELECT * FROM RANKS WHERE RANKS_ID=?";
    public static final String UPDATE_RANK="UPDATE FROM RANKS SET NAME=?, MIN_ORDERS=?, MIN_COMMENTS=?, SALE_PERIOD=?, " +
            "SALE_VALUE=? IS_ELITE=? WHERE RANKS_ID = ?";
    public static final String DELETE_RANK="DELETE FROM RANKS WHERE RANKS_ID = ?";
    public static final String INSERT_ELITE_RANK="INSERT INTO ELITE_RANKS VALUES(ELITE_RANKS_SEQUENCE.nextval, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_ELITE_RANK="SELECT * FROM ELITE_RANKS";
    public static final String SELECT_ELITE_RANK_BY_RANK_ID="SELECT * FROM ELITE_RANKS WHERE RANKS_ID=?";
    public static final String SELECT_ELITE_RANK_USER_INFO="SELECT RANKS.RANKS_ID, RANKS.NAME, RANKS.MIN_ORDERS, RANKS.MIN_COMMENTS, RANKS.SALE_PERIOD,\n" +
            "RANKS.SALE_VALUE, CARCLASSES.NAME, ELITE_RANKS.PERIOD, ELITE_RANKS.FREE_ORDERS\n" +
            "FROM RANKS\n" +
            "JOIN elite_ranks\n" +
            "ON RANKS.RANKS_ID = elite_ranks.ranks_id\n" +
            "JOIN carclasses\n" +
            "ON carclasses.cc_id = elite_ranks.cc_id";
    public static final String UPDATE_ELITE_RANK="UPDATE FROM ELITE_RANKS SET RANKS_ID=?, CC_ID=?, PERIOD=?, FREE_ORDERS=?, " +
            "WHERE E_R_ID = ?";
    public static final String DELETE_ELITE_RANK="DELETE FROM ELITE_RANKS WHERE E_R_ID = ?";

    public static final String INSERT_USER_RANK_ACHIEVEMENT_INFO="INSERT INTO USERS_RANK_ACHIEVEMENT_INFO VALUES(" +
            "USERS_RANK_ACHIEVEMENT_INFO.SEQUENCE.nextval, ?, ?, ?, ?, ?)";

    public static final String SELECT_USER_RANK_ACHIEVEMENT_INFO="SELECT * FROM USERS_RANK_ACHIEVEMENT_INFO";
    public static final String SELECT_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID="SELECT * FROM USERS_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=?";
    public static final String SELECT_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID="SELECT * FROM USERS_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=? AND RANKS_ID=?";
    public static final String SELECT_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID_AND_DRIVER_ID="SELECT * FROM USERS_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=? AND RANKS_ID=?";

    public static final String UPDATE_USER_RANK_ACHIEVEMENT_INFO="UPDATE FROM USERS_RANK_ACHIEVEMENT_INFO SET DATE_URI=?," +
            "USERS_ID=?, RANKS_ID=?, NUMBER_OF_USES_SALE=?, DEADLINE_DATE_SALE=? WHERE URI_ID=?";

    public static final String DELETE_USER_RANK_ACHIEVEMENT_INFO="DELETE * FROM USERS_RANK_ACHIEVEMENT_INFO WHERE URI_ID=?";
    public static final String CHECK_IF_EXIST_USER_RANK="SELECT COUNT(1) FROM USERS_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=?";

    public static final String INSERT_USER_ELITE_RANK_ACHIEVEMENT_INFO="INSERT INTO USERS_ELITE_RANK_ACHIEVEMENT_INFO VALUES(" +
            "USERS_ELITE_RANK_ACHIEVEMENT_INFO.SEQUENCE.nextval, ?, ?, ?, ?, ?, ?";
    public static final String SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO="SELECT * FROM USERS_ELITE_RANK_ACHIEVEMENT_INFO";
    public static final String SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID="SELECT * FROM USERS_ELITE_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=?";
    public static final String SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID="SELECT user_elite_rank_achievement_info.UERAI_ID, \n" +
            "user_elite_rank_achievement_info.DATE_UERAI, \n" +
            "user_elite_rank_achievement_info.USERS_ID,\n" +
            "user_elite_rank_achievement_info.E_R_ID,\n" +
            "user_elite_rank_achievement_info.NUMBER_OF_USES_FREE_ORDER,\n" +
            "user_elite_rank_achievement_info.DEADLINE_DATE_FREE_ORDER,\n" +
            "user_elite_rank_achievement_info.CC_ID\n" +
            "FROM user_elite_rank_achievement_info\n" +
            "JOIN USERS\n" +
            "ON USERS.USERS_ID=user_elite_rank_achievement_info.USERS_ID\n" +
            "JOIN ELITE_RANKS\n" +
            "ON USERS.RANKS_ID=ELITE_RANKS.RANKS_ID AND user_elite_rank_achievement_info.E_R_ID=ELITE_RANKS.E_R_ID\n" +
            "WHERE USERS.USERS_ID=? AND USERS.RANKS_ID=?";

    public static final String SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID_AND_DRIVER_ID="SELECT user_elite_rank_achievement_info.UERAI_ID,\n" +
            "user_elite_rank_achievement_info.DATE_UERAI,\n" +
            "user_elite_rank_achievement_info.USERS_ID,\n" +
            "user_elite_rank_achievement_info.E_R_ID,\n" +
            "user_elite_rank_achievement_info.NUMBER_OF_USES_FREE_ORDER,\n" +
            "user_elite_rank_achievement_info.DEADLINE_DATE_FREE_ORDER,\n" +
            "user_elite_rank_achievement_info.CC_ID\n" +
            "FROM user_elite_rank_achievement_info\n" +
            "JOIN DRIVERS\n" +
            "ON DRIVERS.DRIVERS_ID=DRIVERS_ID\n" +
            "JOIN CARS\n" +
            "ON CARS.CARS_ID=DRIVERS.CARS_ID AND user_elite_rank_achievement_info.CC_ID=CARS.CC_ID\n" +
            "JOIN USERS\n" +
            "ON USERS.USERS_ID=user_elite_rank_achievement_info.USERS_ID\n" +
            "JOIN ELITE_RANKS\n" +
            "ON USERS.RANKS_ID=ELITE_RANKS.RANKS_ID AND user_elite_rank_achievement_info.E_R_ID=ELITE_RANKS.E_R_ID\n" +
            "WHERE USERS.USERS_ID=? AND USERS.RANKS_ID=? AND DRIVERS_ID=?";
    public static final String UPDATE_USER_ELITE_RANK_ACHIEVEMENT_INFO="UPDATE FROM USERS_ELITE_RANK_ACHIEVEMENT_INFO SET DATE_UERAI=?," +
            "USERS_ID=?, E_R_ID=?, NUMBER_OF_USES_FREE_ORDER=?, DEADLINE_DATE_FREE_ORDER=?, CC_ID=? WHERE UERAI_ID=?";

    public static final String DELETE_USER_ELITE_RANK_ACHIEVEMENT_INFO="DELETE * FROM USERS_ELITE_RANK_ACHIEVEMENT_INFO WHERE UERAI_ID=?";
    public static final String CHECK_IF_EXIST_USER_ELITE_RANK="SELECT COUNT(1) FROM USERS_ELITE_RANK_ACHIEVEMENT_INFO WHERE USERS_ID=?";
    public static final String SELECT_DRIVER_INFO_BY_ID="SELECT DRIVERS.DRIVERS_ID, FULLNAME.FIRSTNAME, FULLNAME.SURNAME, CARS.PRODUCER, CARS.BRAND,\n" +
            "CARS.IN_ORDER\n" +
            "FROM DRIVERS\n" +
            "JOIN CARS\n" +
            "ON DRIVERS.CARS_ID = CARS.CARS_ID\n" +
            "JOIN FULLNAME\n" +
            "ON DRIVERS.FULLNAME_ID = FULLNAME.FULLNAME_ID\n" +
            "WHERE DRIVERS.DRIVERS_ID = ?";

    public static final String INSERT_MILITARY_BONUSES="INSERT INTO MILITARY_BONUSES VALUES(" +
            "MILITARY_BONUSES_SEQUENCE.nextval, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_MILITARY_BONUSES="SELECT * FROM MILITARY_BONUSES";
    public static final String SELECT_ALL_MILITARY_BONUSES_BY_ID="SELECT * FROM MILITARY_BONUSES WHERE M_B_ID=?";
    public static final String SELECT_ALL_MILITARY_BONUSES_BY_USER_ID_WITHOUT_PHOTO="SELECT M_B_ID, USERS_ID, STATUS, SALE_VALUE, DATE_M_B FROM MILITARY_BONUSES WHERE USERS_ID=?";
    public static final String UPDATE_MILITARY_BONUSES="UPDATE FROM MILITARY_BONUSES SET USERS_ID=?, DOCUMENT_PHOTO=?, STATUS=?, SALE_VALUE=?, DATE_M_B=>, WHERE M_B_ID=?";
    public static final String DELETE_MILITARY_BONUSES="DELETE FROM MILITARY_BONUSES WHERE M_B_ID=?";

}
