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

  if(selectedIndex<5){
    comment.classList.remove("good");
    comment.style.opacity = 0.7;
  }else{
    comment.classList.add("good");
    comment.style.opacity = 1;
  }

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

  document.querySelectorAll(".keywordBtn").forEach(function (button,index) {
      button.addEventListener("click", function () {
          toggleKeywordsBtnClass(button);
          togglekeywordsRatingArea(index) 
      });
  });

  function toggleKeywordsBtnClass(button) {
      button.classList.toggle("unselected");
      button.classList.toggle("selected");
  }
  
  function togglekeywordsRatingArea(index) {
    var targetDiv = document.querySelector('#keywordEvaluateSection .keywordEvaluateArea:nth-child(' + (index + 1) + ')');
    
    targetDiv.classList.toggle('hidden');
  }

  // ---- 바형 ui에서 선택 효과 로직 ---- //

var keywordEvaluateAreas = document.querySelectorAll('.keywordEvaluateArea');

keywordEvaluateAreas.forEach(function(keyword) {
  var selectPoints = keyword.querySelectorAll('.select-point');

  selectPoints.forEach(function(point, index) {
      point.addEventListener('click', function() {
          toggleSelectPoint(index + 1, keyword);
      });
  });
});

function toggleSelectPoint(rating, keyword) {
  var selectPoints = keyword.querySelectorAll('.select-point');
  var ratingParagraphs = keyword.querySelectorAll('.bar-comment-area p');

  // 해당 keyword 내에서 모든 select-point에서 picked 클래스 제거
  selectPoints.forEach(function(point) {
      point.classList.remove('picked');
  });

  // 클릭된 select-point에 picked 클래스 추가
  keyword.querySelector('.select-point.lv' + rating).classList.add('picked');

  // 각 p 태그에 bold 클래스를 toggle
  ratingParagraphs.forEach(function(p, index) {
      p.classList.toggle('bold', index + 1 === rating);
  });
}


  // ----------제출 버튼 눌림효과 로직---------- //

  var submitBtn = document.getElementById('submitBtn');

  submitBtn.addEventListener('mousedown', function() {
    submitBtn.classList.add('pushed');
  });

  submitBtn.addEventListener('mouseup', function() {
    submitBtn.classList.remove('pushed');
  });

});