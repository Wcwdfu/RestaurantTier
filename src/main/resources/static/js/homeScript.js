function closeBanner() {
    $('#header3').hide();
}

$(document).ready(function () {
    const drawingButton = $('#drawingButton');
    const drawingResult = $('#drawingResult');
    const stopButton = $('#stopButton');
    let isStopped = true;

    drawingButton.on('click', function () {
        fetchData();
    });
    stopButton.on('click', function() {
        isStopped = true;
    })

    function fetchData() {
        $.ajax({
            url: '/api/restaurants?cuisine='+getSelectedOption(), // 실제 API 엔드포인트로 변경
            type: 'GET',
            success: function (data) {
                data = shuffleArray(data);
                isStopped = false;
                handleData(data);
            },
            error: function (error) {
                console.log(this.url);
                console.error('Error fetching data:', error);
            }
        });
    }

    function handleData(data) {
        // 데이터가 비어있으면 아무것도 처리하지 않음
        if (data.length === 0) {
            drawingResult.html(`<p style="color: #000;">데이터가 없습니다.</p>`);
            return;
        }
        let i = 0;
        const maxIndex = data.length;
        function showNextRestaurant() {
            const restaurant = data[i];
            const color = getRandomColor();
            const hrefValue = '/restaurants/' + restaurant.restaurantId;
            const resultHtml = `<a style="color: ${color};">${restaurant.restaurantName}</a>`;
            drawingResult.html(resultHtml);
            if (maxIndex === 1) {
                return;
            }
            if (!isStopped) {
                document.getElementById("cuisineSelect").disabled = true;
                document.getElementById("drawingButton").style.display = 'none';
                document.getElementById("stopButton").style.display = 'inline-block';
                document.getElementById("drawingResult").style.borderColor = '#cb1717';
                i++;
                if (i >= maxIndex) {
                    i = 0;
                }
                setTimeout(showNextRestaurant, 50);
            } else {
                clearTimeout(showNextRestaurant);
                drawingResult.find('a').attr('href', hrefValue);
                document.getElementById("cuisineSelect").disabled = false;
                document.getElementById("drawingButton").style.display = 'inline-block';
                document.getElementById("stopButton").style.display = 'none';
                document.getElementById("drawingResult").style.borderColor = '#5383E8';
            }
        }
        showNextRestaurant();
    }

    function getRandomColor() {
        // 랜덤한 색상을 생성하는 함수
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }
});

function getSelectedOption() {
    var selectElement = document.getElementById("cuisineSelect");
    var selectedValue = selectElement.options[selectElement.selectedIndex].value;

    return selectedValue;
}