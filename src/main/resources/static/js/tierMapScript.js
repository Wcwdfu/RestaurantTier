//const body = document.getElementsByTagName('body')[0];
var latitude = parseFloat(mapInfo.getAttribute('data-latitude'));
var longitude = parseFloat(mapInfo.getAttribute('data-longitude'));
var mapZoom = parseInt(mapInfo.getAttribute('data-zoom'));
var restaurantList = JSON.parse(mapInfo.getAttribute('data-restaurantList'));
var favoriteRestaurantList = JSON.parse(mapInfo.getAttribute('data-favoriteRestaurantIdList'));
console.log(favoriteRestaurantList);
// 네이버 지도
var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(latitude, longitude),
    zoom: mapZoom,
    minZoom: 10,
});

var markers = [];
var tierRestaurantCount = 0;
var bounds = map.getBounds(), // 마커가 현재 화면에서만 표시되도록 하기 위함.
    southWest = bounds.getSW(),
    northEast = bounds.getNE(),
    lngSpan = northEast.lng() - southWest.lng(),
    latSpan = northEast.lat() - southWest.lat();

// 마커 생성
for (var i = 0; i < restaurantList.length; i++) {
    let restaurant = restaurantList[i].restaurant;
    var marker;
    tierRestaurantCount++;
    if (restaurant.mainTier === 1) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            icon: {
                url: '/img/tier/1tier.png',
                size: new naver.maps.Size(25, 25),
                scaledSize: new naver.maps.Size(25, 25),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(12, 25)
            },
            zIndex: 9999
        });
    } else if (restaurant.mainTier === 2) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            icon: {
                url: '/img/tier/2tier.png',
                size: new naver.maps.Size(38, 58),
                scaledSize: new naver.maps.Size(25, 25),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(12, 25)
            },
            zIndex: 9998
        });
    } else if (restaurant.mainTier === 3) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            icon: {
                url: '/img/tier/3tier.png',
                size: new naver.maps.Size(38, 58),
                scaledSize: new naver.maps.Size(25, 25),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(12, 25)
            },
            zIndex: 9997
        });
    } else if (restaurant.mainTier === 4) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            icon: {
                url: '/img/tier/4tier.png',
                size: new naver.maps.Size(38, 58),
                scaledSize: new naver.maps.Size(25, 25),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(12, 25)
            },
            zIndex: 9996
        });
    } else if (restaurant.mainTier === 5) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            icon: {
                url: '/img/tier/5tier.png',
                size: new naver.maps.Size(38, 58),
                scaledSize: new naver.maps.Size(25, 25),
                origin: new naver.maps.Point(0, 0),
                anchor: new naver.maps.Point(12, 25)
            },
            zIndex: 9995
        });
    } else if (restaurant.mainTier === -1) {
        marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
            zIndex: 9994
        });
        tierRestaurantCount--;
    }
    markers.push(marker);
}
// 티어 있는 식당 마커 지도와 연결 = 지도에 표시
for (var i = 0; i< tierRestaurantCount; i++) {
    showMarker(map, markers[i]);
}
/*for (var i = tierRestaurantCount; i < markers.length ; i++) {
    if (i % 10 === 0) {
        showMarker(map, markers[i]);
    }
}*/


// 화면 이동이나 확대, 축소 후 마커 표시되는것 달라지게
naver.maps.Event.addListener(map, 'idle', function() {
    updateMarkers(map, markers);
});
function updateMarkers(map, markers) {

    var mapBounds = map.getBounds();
    var marker, position;

    for (var i = 0; i < markers.length; i++) {
        let zoom = map.getZoom();
        marker = markers[i];
        position = marker.getPosition();
        if (zoom < 17) { // zoom이 17보다 작을 경우 - 티어 있는 식당만 표시
            if (mapBounds.hasLatLng(position) && i < tierRestaurantCount) {
                showMarker(map, marker);
            } else {
                hideMarker(map, marker);
            }
            continue;
        }
        if (zoom === 17) { // zoom이 17일 경우 - 티어 없는 것 25% 표시
            if (mapBounds.hasLatLng(position) && (i < tierRestaurantCount || i % 4 === 0)) {
                showMarker(map, marker);
            } else {
                hideMarker(map, marker);
            }
            continue;
        }
        if (mapBounds.hasLatLng(position)) { // zoom이 17보다 큰 경우 - 전부 표시
            showMarker(map, marker);
        } else {
            hideMarker(map, marker);
        }
    }
}
function showMarker(map, marker) {

    if (marker.getMap()) return;
    marker.setMap(map);
}

function hideMarker(map, marker) {

    if (!marker.getMap()) return;
    marker.setMap(null);
}


// 지도 열기 버튼
const mapArea = document.getElementById('mapArea');
let isMapOpen = false;
document.getElementById('mapOpenButton').addEventListener('click', function() {
    isMapOpen = true;
    history.pushState({}, '');
    body.classList.add('prevent-scroll');
    mapArea.style.display = 'flex';
    resizeMap();
});
// 지도가 열려있을 경우 뒤로가기 동작 지도가 닫히는 동작으로 대체
window.onpopstate = function() {
    if (isMapOpen) {
        closeMap();
    }
}
// 지도 닫기 버튼
document.getElementById('mapCloseButton').addEventListener('click', function() {
    history.back();
})
function closeMap() {
    isMapOpen = false;
    body.classList.remove('prevent-scroll');
    mapArea.style.display = 'none';
}
// 네이버 지도 리사이즈
function resizeMap(){
    let screenWidth = window.innerWidth;
    let screenHeight = window.innerHeight;
    var Size = new naver.maps.Size(screenWidth - 10, screenHeight - 10);
    map.setSize(Size);
}
window.addEventListener('resize', resizeMap);