const searchEl = document.querySelector('.search');
const searchInputEl = searchEl.querySelector('input');


searchEl.addEventListener('click', function(){
  searchInputEl.focus();
});

searchInputEl.addEventListener('focus',function(){
  searchEl.classList.add('focused');
  searchInputEl.setAttribute('placeholder', '통합검색');
});

searchInputEl.addEventListener('blur', function(){
  searchEl.classList.remove('focused');
  searchInputEl.setAttribute('placeholder','');
});

const maincategory = document.getElementsByClassName('main_category_mypage')[0];
const maincategory_community = document.getElementsByClassName('main_category_community')[0];
const subcategory = document.querySelector('.sub_category_mypage');
const subcategory_community = document.querySelector('.sub_category_community')
const wrapsubcategory = document.querySelector('.wrap_sub_category_mypage');
const wrapsubcategory_community = document.querySelector('.wrap_sub_category_community');
const mainframe = document.querySelector('.mainframe');

//마이페이지 버튼 호버링
maincategory.addEventListener('mouseover', function(){
  console.log("콘솔");
  subcategory.classList.remove('hide');
  wrapsubcategory.style.display = "flex";
  mainframe.style.marginTop = "99px";
  mainframe.style.transition = "margin .4s";
});

maincategory.addEventListener('mouseout', function(){
  subcategory.classList.add('hide');
  wrapsubcategory.style.display = "none";
  mainframe.style.marginTop = "40px";
  mainframe.style.transition = "margin .4s";
});

//커뮤니티 버튼 호버링
maincategory_community.addEventListener('mouseover', function(){
  subcategory_community.classList.remove('hide');
  wrapsubcategory_community.style.display = "flex";
  mainframe.style.marginTop = "99px";
  mainframe.style.transition = "margin .4s";
})
maincategory_community.addEventListener('mouseout', function(){
  subcategory_community.classList.add('hide');
  wrapsubcategory_community.style.display = "none";
  mainframe.style.marginTop = "40px";
  mainframe.style.transition = "margin .4s";
});