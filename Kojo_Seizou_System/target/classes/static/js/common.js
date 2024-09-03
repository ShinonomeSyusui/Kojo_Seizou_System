/* 現在時間の取得 */
var today = new Date();
function LoadProc() {
  var nengappi = document.getElementById("DateTimeDisp");
  /* 年 */
  var Year = today.getFullYear();
  /* 月 */
  var Month = today.getMonth()+1;
  if (Month < 10) {
    Month = "0" + Month;
  }
  /* 日 */
  var Date = today.getDate();
  if (Date < 10) {
    Date = "0" + Date;
  }
  /* 時 */
  var Hour = today.getHours();
  if (Hour < 10) {
    Hour = "0" + Hour;
  }
  /* 分 */
  var Min = today.getMinutes();
  if (Min < 10) {
    Min = "0" + Min;
  }
  /* 秒 */
  var Sec = today.getSeconds();
  if (Sec < 10) {
    Sec = "0" + Sec;
  }
  /* 合計 */
  nengappi.innerHTML = "ログイン日時：" + Year + "/" + Month + "/" + Date + " " + Hour + ":" + Min + ":" + Sec + " ";
};

/* openクラスの付け外し */
$(function() {
  $('.js_btn').on('click', function() {
	$('.menu, .btn, .btn-line').toggleClass('open');
  })
});

/* メニューから外れた場合、元に戻す */
function removeClassOpen() {
	var menu = $('.menu');
	if (menu.hasClass('open')) {
		$('.menu, .btn, .btn-line, nav_list').removeClass('open');
	}
};

/* ログアウト処理 */
function logoutClick() {
	/*document.getElementById('.logout').click();*/
	var message ="ログアウトします。\r\nよろしいですか？";

    if (confirm(message)) {
		window.location.href = 'about:blank';
	}
}
