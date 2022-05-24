import com.pedrocosta.utils.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    private Date date;

    @BeforeEach
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();
    }

    @AfterEach
    public void tearDown() {
        this.date = null;
    }

    @Test
    public void testStringToDate() {
        Date expected = date;
        assert expected.equals(DateUtils.stringToDate("2022-05-10"));
        assert expected.equals(DateUtils.stringToDate("05/10/2022", "MM/dd/yyyy"));
    }

    @Test
    public void testDateToString() {
        String expected1 = "2022-05-10";
        assert expected1.equals(DateUtils.dateToString(date));
        String expected2 = "05/10/2022";
        assert expected2.equals(DateUtils.dateToString(date, "MM/dd/yyyy"));
    }

    @Test
    public void testAddDays_1() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addDay(this.date, 1);
        assert expected.equals(result);
    }

    @Test
    public void testAddDays_10() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 20, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addDay(this.date, 10);
        assert expected.equals(result);
    }

    @Test
    public void testAddDays_40() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.JUNE, 19, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addDay(this.date, 40);
        assert expected.equals(result);
    }

    @Test
    public void testAddMonths_1() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.JUNE, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addMonth(this.date, 1);
        assert expected.equals(result);
    }

    @Test
    public void testAddMonths_10() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.MARCH, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addMonth(this.date, 10);
        assert expected.equals(result);
    }

    @Test
    public void testAddMonths_40() {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.SEPTEMBER, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addMonth(this.date, 40);
        assert expected.equals(result);
    }

    @Test
    public void testAddYears_1() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addYear(this.date, 1);
        assert expected.equals(result);
    }

    @Test
    public void testAddYears_10() {
        Calendar cal = Calendar.getInstance();
        cal.set(2032, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addYear(this.date, 10);
        assert expected.equals(result);
    }

    @Test
    public void testAddYears_40() {
        Calendar cal = Calendar.getInstance();
        cal.set(2062, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addYear(this.date, 40);
        assert expected.equals(result);
    }

    @Test
    public void testAddPeriod() {
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 11, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date expected = cal.getTime();
        Date result = DateUtils.addPeriod(this.date, DateUtils.S_DAY, 1);
        assert expected.equals(result);

        cal.set(2022, Calendar.JUNE, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        expected = cal.getTime();
        result = DateUtils.addPeriod(this.date, DateUtils.S_MONTH, 1);
        assert expected.equals(result);

        cal.set(2023, Calendar.MAY, 10, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        expected = cal.getTime();
        result = DateUtils.addPeriod(this.date, DateUtils.S_YEAR, 1);
        assert expected.equals(result);
    }
}
