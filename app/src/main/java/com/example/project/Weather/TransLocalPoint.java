package com.example.project.Weather;

// 날씨 API는 격자좌표를 사용하는데 GpsTracker 클래스를 사용하면 위도경도데이터로 받아지기 때문에 위도경도 데이터를 격자좌표로 변경해주는 클래스

public class TransLocalPoint {

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    //x,y좌표로 변환해주는 것
    public LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y){
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; //격자 간격(km)
        double SLAT1 = 30.0; //투영 위도1(degree)
        double SLAT2 = 60.0; //투영 위도2(degree)
        double OLON = 126.0; //기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; //기준점 x좌표(GRID)
        double YO = 136; //기준점 y좌표(GRID)


        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도, lng_Y:경도). "TOGPS"(좌표->위경도, lat_X:x lat_Y:y))

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if(mode == TO_GRID){
            rs.lat = lat_X; //gps 좌표 위도
            rs.lng = lng_Y; //gps 좌표 경도
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if(theta > Math.PI) theta -= 2.0 * Math.PI;
            if(theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else{
            rs.x = lat_X; //기존의 x좌표
            rs.y = lng_Y; //기존의 y좌표
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn * yn * yn);
            if(sn < 0.0){
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if(Math.abs(xn) <= 0.0){
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0){
                    theta = Math.PI * 0.5;
                    if(xn < 0.0){
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn,yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG; //gps 좌표 위도
            rs.lng = alon * RADDEG; //gps 좌표 경도
        }
        return rs;
    }

    public class LatXLngY{
        public double lat;
        public double lng;

        public double x;
        public double y;
    }
}

