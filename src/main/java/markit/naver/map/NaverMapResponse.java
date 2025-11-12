package markit.naver.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverMapResponse {
    private Status status;
    private List<Result> results;

    // Getters
    public Status getStatus() { return status; }
    public List<Result> getResults() { return results; }

    // Status 내부 클래스
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private int code;
        private String name;
        private String message;
        // Getters
        public int getCode() { return code; }
        public String getName() { return name; }
        public String getMessage() { return message; }
    }

    // Result 내부 클래스 (주소 정보를 담고 있음)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String name;
        private Region region;
        // Getters
        public String getName() { return name; }
        public Region getRegion() { return region; }
    }

    // Region 내부 클래스 (실제 주소 이름)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Region {
        private Area area1; // 시/도
        private Area area2; // 시/군/구
        private Area area3; // 읍/면/동

        // 주소 전체를 조합해서 반환하는 헬퍼 메소드
        public String getFullName() {
            return (area1 != null ? area1.getName() : "") + " " +
                    (area2 != null ? area2.getName() : "") + " " +
                    (area3 != null ? area3.getName() : "");
        }

        // Getters
        public Area getArea1() { return area1; }
        public Area getArea2() { return area2; }
        public Area getArea3() { return area3; }
    }

    // Area 내부 클래스
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Area {
        private String name;
        // Getter
        public String getName() { return name; }
    }
}
