document.addEventListener("DOMContentLoaded", function () {

  // ----------(first) 메인 평가 선택 로직---------- //

var stars=document.querySelectorAll(".stars");
var comment=document.querySelector("#ratingComment");

stars.forEach(function(star,index){
  star.addEventListener("click",function(){
      setMainRating(index);
  });
});

function setMainRating(selectedIndex){
  stars.forEach(function(star,index){
      if(index<=selectedIndex){
          star.src="/img/evaluation/star-filled.png";
      }else{
          star.src="/img/evaluation/star-empty.png";
      }
  });

  if(selectedIndex==0){
      comment.textContent = "장사를 왜 하는지 모르겠어요";
  } else if(selectedIndex==1){
      comment.textContent = "음.. 다시 오고싶다는 생각은 딱히.. ";
  } else if(selectedIndex==2){
      comment.textContent = "조금 아쉬운 가게에요";
  } else if(selectedIndex==3){
      comment.textContent = "평범한 음식점이에요";
  } else if(selectedIndex==4){
      comment.textContent = "괜찮아요. 다시 방문은 할겁니다";
  } else if(selectedIndex==5){
      comment.textContent = "제법 괜찮아요. 무조건 재방문 합니다";
  } else {
      comment.textContent = "당신을 행복하게 해줄 최고의 맛이에요";
  }

}

// ---------- (second) 버튼 선택 효과 로직---------- //

var categoryBtns = document.querySelectorAll(".categoryBtn");

  categoryBtns.forEach(function(button) {
    button.addEventListener("click", function() {
      toggleCategoryBtnsClass(button);
    });
  });

  function toggleCategoryBtnsClass(button) {
    button.classList.toggle("unselected");
    button.classList.toggle("selected");
  }

  // ---------- (third) 버튼 선택 효과 로직---------- //

  document.querySelectorAll(".keywordBtn").forEach(function (button) {
      button.addEventListener("click", function () {
          toggleKeywordPick(button.parentNode);
          toggleKeywordsBtnClass(button);
          toggleRatingBar(button);
      });
  });
  
  function toggleKeywordPick(keywordDiv) {
      keywordDiv.classList.toggle("picked");
  }
  
  function toggleKeywordsBtnClass(button) {
      button.classList.toggle("unselected");
      button.classList.toggle("selected");
  }
  
  function toggleRatingBar(button) {
      var ratingBar = button.nextElementSibling;
      ratingBar.classList.toggle("hidden");
  }

  // ---- 바형 ui에서 선택 효과 로직 ---- //

  var keywords = document.querySelectorAll('.keyword');

  keywords.forEach(function(keyword) {
    var selectPoints = keyword.querySelectorAll('.select-point');
    var ratingParagraphs = keyword.querySelectorAll('.bar-comment-area p');

    selectPoints.forEach(function(point, index) {
      point.addEventListener('click', function() {
        toggleSelectPoint(index + 1, keyword);
      });
    });
  });

  function toggleSelectPoint(rating, keyword) {
    // 해당 keyword 내에서 모든 select-point에서 picked 클래스 제거
    keyword.querySelectorAll('.select-point').forEach(function(point) {
      point.classList.remove('picked');
    });

    // 클릭된 select-point에 picked 클래스 추가
    keyword.querySelector('.select-point.lv' + rating).classList.add('picked');

    // 각 p 태그에 bold 클래스를 toggle
    keyword.querySelectorAll('.bar-comment-area p').forEach(function(p, index) {
      if (index + 1 === rating) {
        p.classList.toggle('bold');
      } else {
        p.classList.remove('bold');
      }
    });
  }

  // ----------제출 버튼 눌림효과 로직---------- //
  var submitBtn = document.getElementById('submitBtn');

  submitBtn.addEventListener('mousedown', function() {
    submitBtn.style.backgroundColor= '#365392';
  });

  submitBtn.addEventListener('mouseup', function() {
    submitBtn.style.backgroundColor = '#28344E';
  });


});