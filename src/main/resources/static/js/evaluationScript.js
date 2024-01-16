document.addEventListener("DOMContentLoaded", function () {

    // ----------로그인버튼 눌림효과 로직---------- //
  var loginBtn=document.getElementById('loginBtn');
  loginBtn.addEventListener('mousedown',function(){
    loginBtn.classList.add('pushed');
  });

  loginBtn.addEventListener('mouseup',function(){
    loginBtn.classList.remove('pushed');
  });

    // ----------검색 버튼 눌림효과 로직---------- //
  var searchBtn=document.getElementById('searchBtn');
//   var searchIcon=document.getElementById('searchIcon');
  
  searchBtn.addEventListener('mousedown', function() {
    searchBtn.classList.add('pushed');
  });
  
  searchBtn.addEventListener('mouseup', function() {
    searchBtn.classList.remove('pushed');
  });

    // ----------카테고리 복수 선택 버튼 로직---------- //

    var restaurantCategoryBtns = document.querySelectorAll(".optionBtn"); /*클래스가 optionBtn인 모든 요소 선택*/
    restaurantCategoryBtns.forEach(function (button) {
        button.addEventListener("click", function () {
          changeColor(button);
        });
      });


      function changeColor(button) {
        var currentBtnState = button.classList.contains("unselected") ? "unselected" : "selected";

        if (currentBtnState === "unselected") {
          button.classList.remove("unselected");
          button.classList.add("selected");
        } else {
          button.classList.remove("selected");
          button.classList.add("unselected");
    }

     // 추가적으로 선택된 버튼에 대한 동작 또는 로직을 여기에 추가
    // 예를 들어 선택된 버튼의 텍스트를 출력하거나 다른 동작을 수행할 수 있습니다.
    console.log("선택된 버튼: " + button.textContent);
  }

    // ----------메인 평가 선택 로직---------- //

  var stars0=document.querySelectorAll(".restaurant-main-rating .fa-star");
  var comment0=document.querySelector(".maincomment");

  stars0.forEach(function(star,index){
    star.addEventListener("click",function(){
        setMainRating(index);
    });
  });

  function setMainRating(selectedIndex){
    stars0.forEach(function(star,index){
        if(index<=selectedIndex){
            star.style.color="#FFF500";
            star.classList.add("fa-beat");
        }else{
            star.style.color="white";
            star.classList.remove("fa-beat");
        }
    });

    if(selectedIndex==0){
        comment0.textContent = "장사를 왜 하는지 모르겠어요";
    } else if(selectedIndex==1){
        comment0.textContent = "한번 먹어봤으니 이제 다시 여기를 올 일은 없군요";
    } else if(selectedIndex==2){
        comment0.textContent = "음.. 다시 오고싶다는 생각은 딱히.. ";
    } else if(selectedIndex==3){
        comment0.textContent = "평범한 음식점이에요";
    } else if(selectedIndex==4){
        comment0.textContent = "무난해요. 다시 방문은 할겁니다";
    } else if(selectedIndex==5){
        comment0.textContent = "제법 괜찮아요. 무조건 재방문 합니다";
    } else {
        comment0.textContent = "당신을 행복하게 해줄 최고의 맛이에요";
    }

  }

    // ----------가성비 선택 로직---------- //

  var stars1=document.querySelectorAll(".ratingstars-cost-effectiveness .fa-star");
  var comment1=document.querySelector(".ratingstars-cost-effectiveness .subcomment");

  stars1.forEach(function(star,index){
    star.addEventListener("click",function(){
        setCostEffectiveRating(index);
    });
  });

  function setCostEffectiveRating(selectedIndex){
    stars1.forEach(function(star,index){
        if(index<=selectedIndex){
            star.style.color="#FFF500";
            star.classList.add("fa-beat");
        }else{
            star.style.color="white";
            star.classList.remove("fa-beat");
        }
    });

    if(selectedIndex==0){
        comment1.textContent = "경제관념이 의심스러워요";
    } else if(selectedIndex==1){
        comment1.textContent = "가성비가 좋진 않아요";
    } else if(selectedIndex==2){
        comment1.textContent = "평범해요";
    } else if(selectedIndex==3){
        comment1.textContent = "비교적 저렴한 것 같아요";
    } else {
        comment1.textContent = "최고의 가성비에요!";
    }

  }

  // ----------웨이팅 선택 로직---------- //

  var stars2=document.querySelectorAll(".ratingstars-waiting .fa-star");
  var comment2=document.querySelector(".ratingstars-waiting .subcomment");

  stars2.forEach(function(star,index){
    star.addEventListener("click",function(){
        setWaitingRating(index);
    });
  });

  function setWaitingRating(selectedIndex){
    stars2.forEach(function(star,index){
        if(index<=selectedIndex){
            star.style.color="#FFF500";
            star.classList.add("fa-beat");
        }else{
            star.style.color="white";
            star.classList.remove("fa-beat");
        }
    });

    if(selectedIndex==0){
        comment2.textContent = "좌석좀 더 만들어주세요 사장님";
    } else if(selectedIndex==1){
        comment2.textContent = "웨이팅 시간이 제법 긴편이에요";
    } else if(selectedIndex==2){
        comment2.textContent = "피크타임마다 기다려야 하는 편이에요";
    } else if(selectedIndex==3){
        comment2.textContent = "가끔 기다려야할 수 있어요";
    } else {
        comment2.textContent = "전혀 기다리지 않아도 되요";
    }

  }

  // ----------친절도 선택 로직---------- //

  var stars3=document.querySelectorAll(".ratingstars-kindness .fa-star");
  var comment3=document.querySelector(".ratingstars-kindness .subcomment");

  stars3.forEach(function(star,index){
    star.addEventListener("click",function(){
        setKindnessRating(index);
    });
  });

  function setKindnessRating(selectedIndex){
    stars3.forEach(function(star,index){
        if(index<=selectedIndex){
            star.style.color="#FFF500";
            star.classList.add("fa-beat");
        }else{
            star.style.color="white";
            star.classList.remove("fa-beat");
        }
    });

    if(selectedIndex==0){
        comment3.textContent = "기분이가 너무 안좋네요";
    } else if(selectedIndex==1){
        comment3.textContent = "다소 불친절해요";
    } else if(selectedIndex==2){
        comment3.textContent = "평범해요";
    } else if(selectedIndex==3){
        comment3.textContent = "다소 친절해요";
    } else {
        comment3.textContent = "표정부터 행동까지 너무친절해요";
    }

  }

  // ----------제출 버튼 눌림효과 로직---------- //
  var searchBtn=document.getElementById('submitBtn');
  searchBtn.addEventListener('mousedown',function(){
    searchBtn.classList.add('pushed');
  });

  searchBtn.addEventListener('mouseup',function(){
    searchBtn.classList.remove('pushed');
  });

});