import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {
    private static final int REQUIRED_MINUTES_PER_DAY = 8 * 60; // 하루에 필요한 근무 시간을 분 단위로 계산 (8시간 * 60분)
    private static final int LUNCH_BREAK_MINUTES = 60; // 점심시간 1시간을 분 단위로 계산
    private Map<String, Integer> workHoursBalance = new HashMap<>(); // 날짜별 근무 시간 잔액을 저장하는 맵
    private int remainingMinutes = 0; // 초과 근무한 남은 분을 누적하는 변수

    public void addWorkDay(String date, String startTime, String endTime) {
        String[] startTimeParts = startTime.split("h");
        String[] endTimeParts = endTime.split("h");
        
        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1].replace("m", ""));
        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1].replace("m", ""));

        // 근무 시작 시간과 종료 시간을 분 단위로 환산
        int workedMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
        // 점심시간을 근무 시간에서 제외
        workedMinutes -= LUNCH_BREAK_MINUTES;
        // 하루 필요한 근무 시간을 기준으로 근무 시간 잔액 계산
        int balance = workedMinutes - REQUIRED_MINUTES_PER_DAY;

        // 날짜별 근무 시간 잔액 저장
        workHoursBalance.put(date, balance);
    }

    public int calculateEligibleEarlyLeaveDays() {
        // 모든 날짜의 근무 시간 잔액을 합산
        int totalBalance = workHoursBalance.values().stream().mapToInt(Integer::intValue).sum();
        // 누적된 남은 분도 합산
        totalBalance += remainingMinutes;

        // 합산된 잔액을 60분으로 나누어 오후 5시에 퇴근할 수 있는 날 계산
        int eligibleDays = totalBalance / 60;
        // 남은 분 계산 (합산된 잔액을 60으로 나눈 나머지)
        remainingMinutes = totalBalance % 60;

        return eligibleDays;
    }

    public static void main(String[] args) {
        Main calculator = new Main();

        // 근무일 추가: "YYYY-MM-DD", "시작시간h분m", "종료시간h분m"
        calculator.addWorkDay("2023-01-02", "9h00m", "20h34m"); // 예시로 1월 2일에 오전 9시부터 오후 20시 34분까지 근무
        calculator.addWorkDay("2023-01-03", "8h00m", "18h00m");
        calculator.addWorkDay("2023-01-04", "9h00m", "17h00m");

        // 결과 출력
        int earlyLeaveDays = calculator.calculateEligibleEarlyLeaveDays();
        System.out.println("근무 시간 잔액(분 단위): " + calculator.workHoursBalance);
        System.out.println("오후 5시에 퇴근할 수 있는 날: " + earlyLeaveDays + "일");
        System.out.println("남은 초과 근무 시간(분 단위): " + calculator.remainingMinutes + "분");
    }
}
