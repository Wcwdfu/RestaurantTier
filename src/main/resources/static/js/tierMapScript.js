var latitude = parseFloat(mapInfo.getAttribute('data-latitude'));
var longitude = parseFloat(mapInfo.getAttribute('data-longitude'));
var mapZoom = parseInt(mapInfo.getAttribute('data-zoom'));
var restaurantList = JSON.parse(mapInfo.getAttribute('data-restaurantList'));
// 네이버 지도
var map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(latitude, longitude),
    zoom: mapZoom,
    minZoom: 10,
});

for (var i = 0; i < restaurantList.length; i++) {
    let restaurant = restaurantList[i].restaurant;
    var marker = new naver.maps.Marker({
        position: new naver.maps.LatLng(restaurant.restaurantLatitude, restaurant.restaurantLongitude),
        map: map
    });
    console.log(marker);
}

// 지도 열기 버튼
const mapArea = document.getElementById('mapArea');
document.getElementById('mapOpenButton').addEventListener('click', function() {
    let screenWidth = window.innerWidth;
    let screenHeight = window.innerHeight;
    resize(screenWidth - 10, screenHeight - 10)
    mapArea.style.display = 'flex';
});
document.getElementById('mapCloseButton').addEventListener('click', function() {
    mapArea.style.display = 'none';
})
function resize(width, height){
    var Size = new naver.maps.Size(width, height);
    map.setSize(Size);
}